function addShowInfoEvent() {
    for (let i = 0; i < placeInfos.length; i++) {
        // 클릭 시 상세정보 표시
        placeTitles[i].addEventListener("click", function () {
            // 전에 클릭해서 표시된 항목을 숨김
            hideLastClicked();
            var placeInfo = document.getElementById(this.id + "-info");
            shownPlaceInfo = placeInfo;
            showClickedPlace(placeInfo);
            let place = parsePlaceObject(this, placeList);
            setMapCenter(map, place);

            let closeButton = document.getElementById('closeButton');
            closeButton.style.display = "block";
        });
    }
}

function addHideInfoEvent() {
    // for (let i = 0; i < placeInfos.length; i++) {
    //     placeInfos[i].addEventListener("click", function () {
    //         this.style.display = "none";
    //         placePanel.style.display = "none";
    //     });
    // }
    let closeButton = document.getElementById('closeButton');
    closeButton.addEventListener("click", function () {
        shownPlaceInfo.style.display = "none";
        placePanel.style.display = "none";
        this.style.display = "none";
    })
}

function hideLastClicked() {
    if (lastClicked != undefined) {
        lastClicked.style.display = "none";
        placePanel.style.display = "none";
    }
}

function showClickedPlace(placeInfo) {
    lastClicked = placeInfo;
    placePanel.style.display = "block";
    placeInfo.style.display = "block";
}

function addDistanceChangeEvent() {
    document.getElementById('distanceSelectBox').addEventListener('change', function () {
        let selectedValue = this.value;
        let currentUrl = window.location.href;
        let url = new URL(currentUrl);
        url.searchParams.set("distance-within", selectedValue);
        window.location.href = url.toString();
    });
}