function parsePlaceObject(placeTitle, placeList) {
    const regex = /[^0-9]/g;
    const result = placeTitle.id.replace(regex, "");
    const number = parseInt(result);
    return placeList.find(place => place.id === number);
}
