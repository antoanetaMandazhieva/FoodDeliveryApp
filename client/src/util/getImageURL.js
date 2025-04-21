export const getCuisineImageUrl = (identifier, name) => {
    return new URL(`../assets/images/${identifier}/${name}`, import.meta.url).href;
}

export const getRestaurantLogoUrl = (identifier, name) => {
    return new URL(`../assets/images/${identifier}/logos/${name}`, import.meta.url).href;
}

export const getRestaurantProductUrl = (identifier, name) => {
    return new URL(`../assets/images/${identifier}/products/${name}`, import.meta.url).href;
}