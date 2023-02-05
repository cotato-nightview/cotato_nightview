function showMap(defaultLatitude, defaultLongitude) {
    // 지도 표시
    var container = document.getElementById('map');
    var options = {
        center: new kakao.maps.LatLng(defaultLatitude, defaultLongitude),
        level: 5
    };
    return new kakao.maps.Map(container, options);
}

function makeMarkers(map, placeList) {
    placeList.forEach(place => {
        makeMarkerAndOverlay(map, place);
    })
}

function makeMarkerAndOverlay(map, place) {
    var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

    // 마커 이미지의 이미지 크기 입니다
    var imageSize = new kakao.maps.Size(24, 35);

    // 마커 이미지를 생성합니다
    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

    var position = new kakao.maps.LatLng(place.latitude, place.longitude)

    var marker = makeMarker(map, place, position, markerImage);
    var overlay = makeOverlay(map, place, position);
    addOverlayListener(map, place, marker, overlay);
}

// 마커 생성 함수
function makeMarker(map, place, position, markerImage) {
    return new kakao.maps.Marker({
        // 마커를 표시할 지도
        map: map,
        // 마커를 표시할 위치
        position: position,
        // 마커 이미지
        image: markerImage
    });
}

// 오버레이 생성 함수
function makeOverlay(map, place, position) {
    var content = document.createElement('button');
    content.className = "btn btn-primary";
    content.innerText = place.title;
    content.id = 'overlay-' + place.id;
    // 오버레이 오브젝트 생성
    return new kakao.maps.CustomOverlay({
        content: content,
        map: map,
        position: position
    });
}

// 오버레이 리스너 추가 함수
function addOverlayListener(map, place, marker, overlay) {
    // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
    kakao.maps.event.addListener(marker, 'mouseover', function () {
        // mouseover 시에 오버레이 나타나게 리스터 추가
        overlay.setMap(map);
        // 오버레이 위치 조정
        var overlayElement = document.getElementById('overlay-' + place.id).parentElement;
        var top = parseInt(overlayElement.style.top, 10) - 60;
        overlayElement.style.top = top + 'px';
    });
    kakao.maps.event.addListener(marker, 'mouseout', function () {
        // mouseout 시에 오버레이 없어지게 리스터 추가
        overlay.setMap(null);
    });

    // 처음 로딩 시엔 안나타나게 설정
    overlay.setMap(null);
}

