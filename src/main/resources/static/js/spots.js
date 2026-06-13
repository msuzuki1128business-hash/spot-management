/**
 * 前日の日付（YYYY-MM-DD形式）を取得
 * targetDate を基準に -1日する
 */
function getPrevDate() {
    const prevDay = new Date(
        targetDate.year,
        targetDate.monthValue - 1,
        targetDate.dayOfMonth
    );

    // 1日前にずらす
    prevDay.setDate(prevDay.getDate() - 1);

    // YYYY-MM-DD形式に整形して返す
    return (
        prevDay.getFullYear() + '-' +
        String(prevDay.getMonth() + 1).padStart(2, '0') + '-' +
        String(prevDay.getDate()).padStart(2, '0')
    );
}

/**
 * 翌日の日付（YYYY-MM-DD形式）を取得
 * targetDate を基準に +1日する
 */
function getNextDate() {
    const nextDay = new Date(
        targetDate.year,
        targetDate.monthValue - 1,
        targetDate.dayOfMonth
    );

    // 1日進める
    nextDay.setDate(nextDay.getDate() + 1);

    // YYYY-MM-DD形式に整形して返す
    return (
        nextDay.getFullYear() + '-' +
        String(nextDay.getMonth() + 1).padStart(2, '0') + '-' +
        String(nextDay.getDate()).padStart(2, '0')
    );
}

/**
 * サーバーから受け取ったフライトデータを
 * フロント描画用の簡易オブジェクトに変換
 */
const flights = flightsData.map(f => ({
    id: f.id,

    // 到着・出発スポットID（DOMの行特定に使用）
    arrSpotId: f.arrSpot.id,
    depSpotId: f.depSpot.id,

    // 便名
    arrFlightNumber: f.arrFlightNumber,
    depFlightNumber: f.depFlightNumber,

    // 機材種別
    aircraftType: f.arrAircraftType.typeName,

    // 時刻情報（タイムライン描画に使用）
    arrivalTime: f.arrScheduledArrivalTime,
    departureTime: f.depScheduledDepartureTime,

    // 警告フラグ
    sizeWarning: f.sizeWarning,
    intervalWarning: f.intervalWarning
}));

// タイムラインのスケール設定
// 1セル = 15分相当
const CELL_WIDTH = 20;

/**
 * 時刻データを「分」に変換する
 * - object形式（hour/minute）またはDate文字列に対応
 */
function timeToMinutes(t) {
    if (!t) return null;

    // サーバーから来たオブジェクト形式の場合
    if (typeof t === 'object') {
        return t.hour * 60 + t.minute;
    }

    // 文字列 / Date形式の場合（UTC基準で取得）
    const d = new Date(t);
    return d.getUTCHours() * 60 + d.getUTCMinutes();
}

/**
 * フライトバーを画面上に描画する
 * 各スポット行（data-spot-id）に対してDOMを生成する
 */
function renderFlights() {

    flights.forEach(flight => {

        // 到着・出発時刻を分単位に変換
        const arrMin = timeToMinutes(flight.arrivalTime);
        const depMin = timeToMinutes(flight.departureTime);

        // 時刻が不正なら描画しない
        if (arrMin === null || depMin === null) return;

        // 滞在時間（分）
        const duration = depMin - arrMin;

        // マイナスやゼロは無効（描画しない）
        if (duration <= 0) return;

        /**
         * 横位置計算
         * 15分 = CELL_WIDTH px のスケール
         */
        const left = (arrMin / 15) * CELL_WIDTH;
        const width = (duration / 15) * CELL_WIDTH;

        // 該当スポット行を取得
        const row = document.querySelector(
            `[data-spot-id="${flight.arrSpotId}"]`
        );
        if (!row) return;

        const cellsArea = row.querySelector('.cells-area');
        if (!cellsArea) return;

        /**
         * 警告状態に応じてバーの色を変更
         * sizeWarning > intervalWarning の優先順
         */
        let barClass = 'flight-bar flight-normal';

        if (flight.sizeWarning) {
            barClass = 'flight-bar flight-warning-size';
        } else if (flight.intervalWarning) {
            barClass = 'flight-bar flight-warning-interval';
        }

        // フライトバー本体DOM生成
        const bar = document.createElement('div');
        bar.className = barClass;

        // 位置と幅をタイムライン上に反映
        bar.style.left = left + 'px';
        bar.style.width = width + 'px';

        // 中身を横3分割表示（到着便名・機材・出発便名）
        bar.style.display = 'flex';
        bar.style.justifyContent = 'space-between';
        bar.style.alignItems = 'center';
        bar.style.padding = '0 6px';

        // 到着便名
        const arrSpan = document.createElement('span');
        arrSpan.textContent = flight.arrFlightNumber;
        arrSpan.style.fontSize = '10px';

        // 機材タイプ
        const typeSpan = document.createElement('span');
        typeSpan.textContent = flight.aircraftType;
        typeSpan.style.fontSize = '10px';

        // 出発便名
        const depSpan = document.createElement('span');
        depSpan.textContent = flight.depFlightNumber;
        depSpan.style.fontSize = '10px';

        // DOM組み立て
        bar.appendChild(arrSpan);
        bar.appendChild(typeSpan);
        bar.appendChild(depSpan);

        // ホバー時表示用
        bar.title =
            flight.arrFlightNumber + ' → ' + flight.depFlightNumber;

        /**
         * クリックで編集画面へ遷移
         */
        bar.addEventListener('click', () => {
            location.href = '/spots/flights/' + flight.id + '/edit';
        });

        // 画面に追加
        cellsArea.appendChild(bar);
    });
}

// 初回描画
renderFlights();