
// Fetch the JSON file of the properties.
fetch('properties.json')
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }
        return response.json();
    })
    .then(json => initialize(json))
    .catch(error => console.error(`Fetch problem ${error.message}`));

/**
 * This function is meant to:
 * 
 * 1) Grab the UI elements that are going to built onto
 * 2) Define the function to update the display
 * 3) Define's display update's helper functions
 */
function initialize(properties) {
    
    const filter = document.querySelector("#filter");
    const query = document.querySelector("#query");
    const searchBtn = document.querySelector("button");
    const results = document.querySelector("#results");

    //Store last filter used for later
    let lastFilter = filter.value;
    let lastSearch = '';

    let filteredGroup = properties; //Set to properties by default
    let finalGroup = []; //Group as a result of being filtered by search

    searchBtn.addEventListener("click", filterGroup);

    /**
     * Checks for search term value amongst given filter key. 
     * e.g. search for [query] in [property.filter.value]
     */
    function filterGroup(e) {
        //Prevent submit button from submitting the form.
        e.preventDefault();

        //if last search and filter remain the same, just return out of function automatically
        if(lastFilter == filter.value && lastSearch == query.value.trim()) {
            return;
        }

        //Store search and filter as new "lasts" for next search
        lastFilter = currFilter = filter.value;
        lastSearch = currSearch = query.value.trim();

        finalGroup = filteredGroup.filter((property) => property[`${currFilter}`].toString().includes(currSearch));
        //finalGroup = filteredGroup;
        console.log(filteredGroup[0][`${currFilter}`]);
        console.log(finalGroup);
        updateDisplay();
    }
    
    function updateDisplay() {

        //Empty/reset results page.
        results.removeChild(results.firstChild); 
        

        //No matching search results.
        if (finalGroup.length === 0) {
            const para = document.createElement("p");
            para.textContent = "No matching search results.";
            results.appendChild(para);
        } else {
            //Create semantic list container for each entry.
            const list = document.createElement("ul");
            list.setAttribute("class", "entries");
            results.appendChild(list);
            for (const property of finalGroup) {
                list.appendChild(createCard(property));
                console.log("Ready!");
            }
        }
    }

    function createCard(entry) {
        const card = document.createElement("li");
        const address = document.createElement("h2");
        const owner = document.createElement("h3");
        const year = document.createElement("p");
        const value = document.createElement("p");
        const bookNum = document.createElement("p");
        const docNum = document.createElement("p");

        address.textContent = entry.address;
        owner.textContent = `Owner: ${entry.owner}`;
        year.textContent = `Entry Year: ${entry.entryYear}`;
        value.textContent = `Home Value: $${entry.value}`;
        bookNum.textContent = `Book Number: ${entry.bookNumber}`;
        docNum.textContent = `Document Reg. Number: ${entry.docNumber}`;

        card.append(address, owner, year, value, bookNum, docNum);

        return card;
    }



}