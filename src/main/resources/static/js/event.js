function addShowInfoEvent() {
    for (let i = 0; i < placeInfos.length; i++) {
        // 클릭 시 상세정보 표시
        placeTitles[i].addEventListener("click", function () {
            // 전에 클릭해서 표시된 항목을 숨김
            hideLastClicked();
            var placeInfo = document.getElementById(this.id + "-info");
            showClickedPlace(placeInfo);
            let place = parsePlaceObject(this, placeList);
            setMapCenter(map, place);
        });
    }
}

function addHideInfoEvent() {
    for (let i = 0; i < placeInfos.length; i++) {
        placeInfos[i].addEventListener("click", function () {
            this.style.display = "none";
            placePanel.style.display = "none";
        });
    }
}

function hideLastClicked() {
    if (last_clicked != undefined) {
        last_clicked.style.display = "none";
        placePanel.style.display = "none";
    }
}

function showClickedPlace(placeInfo) {
    last_clicked = placeInfo;
    placePanel.style.display = "block";
    placeInfo.style.display = "block";
}