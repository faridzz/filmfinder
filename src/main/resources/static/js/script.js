'use strict';

/**
 * navbar variables
 */

const suggestionContainer = document.getElementById("suggestion-container");
const searchInput = document.getElementById("searchMovie-input")
const navOpenBtn = document.querySelector("[data-menu-open-btn]");
const navCloseBtn = document.querySelector("[data-menu-close-btn]");
const navbar = document.querySelector("[data-navbar]");
const overlay = document.querySelector("[data-overlay]");

const navElemArr = [navOpenBtn, navCloseBtn, overlay];

for (let i = 0; i < navElemArr.length; i++) {

  navElemArr[i].addEventListener("click", function () {

    navbar.classList.toggle("active");
    overlay.classList.toggle("active");
    document.body.classList.toggle("active");

  });

}



/**
 * header sticky
 */

const header = document.querySelector("[data-header]");

window.addEventListener("scroll", function () {

  window.scrollY >= 10 ? header.classList.add("active") : header.classList.remove("active");

});



/**
 * go top
 */

const goTopBtn = document.querySelector("[data-go-top]");

window.addEventListener("scroll", function () {

  window.scrollY >= 500 ? goTopBtn.classList.add("active") : goTopBtn.classList.remove("active");

});

/**
 * suggestion for search 
 */
searchInput.addEventListener("keyup", function() {
  const query = searchInput.value.trim();

  if (query.length === 0) {
    suggestionContainer.innerHTML = "";
    return;
  }
  fetch(`https://www.movie-map.com/typeahead?query=${query}`)
      .then(response => response.text())
      .then(data => {
        const listData = data.split(/\n/);
        console.log(listData);
        renderSuggestions(listData);

      })
      .catch(error => {
        console.error("Error fetching suggestions:", error);
      });
});

function renderSuggestions(suggestions) {
  suggestionContainer.innerHTML = "";
  suggestions.forEach(suggestion => {
    const suggestionItem = document.createElement("li");
    suggestionItem.textContent = suggestion; // or suggestion.title if available
    suggestionItem.classList.add("suggestion-li");
    suggestionContainer.appendChild(suggestionItem);
    suggestionItem.addEventListener("click", function() {
      searchInput.value = suggestion;

      suggestionContainer.innerHTML = "";
      // Additional action on suggestion click
    });
    suggestionContainer.appendChild(suggestionItem);
  });
}
//what do when we start searching
function searching (){
  hideSuggestionContainer()
  searchingAnimation()
  scrollToTop()
  blockEverything()




}
//show searching animation
function searchingAnimation() {

  document.getElementById('loader').style.display = 'block';
}
//hide suggestion container
function hideSuggestionContainer() {
  document.getElementById('suggestion-container').style.display = 'none';
}
//go top of page
function scrollToTop() {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
}
//block every touch when searching
function blockEverything() {
  document.getElementById('top').style.pointerEvents = 'none';
}

