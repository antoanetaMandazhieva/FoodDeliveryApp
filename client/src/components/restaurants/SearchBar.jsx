import { useGSAP } from '@gsap/react';
import gsap from 'gsap';
import { useState, useEffect } from 'react';

const SearchBar = ({ sortIsClicked, handleSortChange, filterIsClicked, handleFilterChange, moreIsClicked, handleMoreIsClicked }) => {
    const [windowWidth, setWindowWidth] = useState(window.innerWidth) 

    const [searchData, setSearchData] = useState(() => '');

    useEffect(() => {
        const handleResize = () => {
            setWindowWidth(window.innerWidth);
        }
        window.addEventListener('resize', handleResize);

        return () => window.removeEventListener('resize', handleResize)
    }, [])

    

    const handleSearchChange = (event) => {
        const {value} = event.target;

        setSearchData(() => value);
    }

    return (
        <section className='max-sm:grid max-sm:grid-flow-col max-sm:grid-cols-3 
            max-sm:grid-rows-2 max-sm:gap-4 flex justify-around items-center 
            w-full bg-ivory pb-2'>

            {/* Sorts */}

            <div className='flex flex-col justify-between items-center sm:w-[35%] md:w-[30%] pt-6'>
                <h1 className='md:text-lg lg:text-xl font-bold font-quicksand'>
                    Sort By
                </h1>


                <div className='flex justify-around items-center pt-3 w-full'>

                    {/* Sort 1 */} 

                    <button id='sort-1' className='flex flex-col justify-between items-center mr-5' onClick={handleSortChange} disabled={filterIsClicked.clicked} data-value='top-rated'>
                        <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' 
                            stroke-width='1.5' stroke='currentColor' 
                            className={`fill-none rounded-full max-sm:size-6 sm:size-7 md:size-8 lg:size-9 max-sm:mb-3 hover:bg-peach-400 ${(sortIsClicked.clicked && sortIsClicked.elementId === 'sort-1') ? 'bg-peach-400' : 'bg-peach-100'}`}   
                        >
                            <path stroke-linecap='round' stroke-linejoin='round' d='M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z' />
                        </svg>
                        <h2 className='max-sm:text-sm sm:text-sm lg:text-md font-quicksand'>
                            Rating
                        </h2>
                    </button>

                    {/* Sort 2 */}

                    <button id='sort-2' className='flex flex-col justify-between items-center mr-5' onClick={handleSortChange} disabled={filterIsClicked.clicked} data-value='sorted/asc'>
                        <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' 
                            stroke-width='1.5' stroke='currentColor' 
                            className={`rounded-full max-sm:size-6 sm:size-7 md:size-8 lg:size-9 hover:bg-peach-400 ${(sortIsClicked.clicked && sortIsClicked.elementId === 'sort-2') ? 'bg-peach-400' : 'bg-peach-100'}`}
                        >
                            <path stroke-linecap='round' stroke-linejoin='round' d='m4.5 19.5 15-15m0 0H8.25m11.25 0v11.25' />
                        </svg>
                        <h2 className='max-sm:text-xs sm:text-sm lg:text-md font-quicksand'>
                            A-Z Asc
                        </h2>
                    </button>

                    {/* Sort 3 */}

                    <button id='sort-3' className='flex flex-col justify-between items-center mr-5' onClick={handleSortChange} disabled={filterIsClicked.clicked} data-value='sorted/desc'>
                        <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' 
                            stroke-width='1.5' stroke='currentColor' 
                            className={`fill-ivory rounded-full max-sm:size-6 sm:size-7 md:size-8 lg:size-9 hover:bg-peach-400 ${(sortIsClicked.clicked && sortIsClicked.elementId === 'sort-3') ? 'bg-peach-400' : 'bg-peach-100'}`}
                        >
                            <path stroke-linecap='round' stroke-linejoin='round' d='m4.5 4.5 15 15m0 0V8.25m0 11.25H8.25'/>
                        </svg>
                        <h2 className='max-sm:text-xs sm:text-sm lg:text-md font-quicksand'>
                            A-Z Desc
                        </h2>
                    </button>
                </div>
            </div>

            {/* Search bar */}

            <input 
                className='max-sm:grid-cols-subgrid max-sm:col-span-3 
                max-sm:place-self-center max-sm:w-[70%] sm:w-[30%] md:w-[40%] 
                rounded-full bg-peach-400 text-center text-black sm:text-md 
                md:text-lg lg:text-xl font-quicksand h-12 
                focus:outline focus:outline-black'
                type='text'
                name='search'
                value={searchData}
                placeholder={`Search for ${windowWidth >= 768 ? 'restaurant suggestions' : 'restaurants'}`}
                onChange={handleSearchChange}
            />

            {/* Filters */}
            <div className='max-sm:grid-cols-subgrid max-sm:col-span-2 max-sm:pl-5 flex flex-col justify-between items-center sm:w-[35%] md:w-[30%] pt-6'>
                <h1 className='md:text-lg lg:text-xl font-bold font-quicksand'>
                    Filters
                </h1>
                <div className='flex justify-around items-center w-full'>

                    {/* Filter 1 */}

                    <button id='4' className='flex flex-col justify-between items-center pt-3' onClick={handleFilterChange} disabled={filterIsClicked.elementId !== '4' && filterIsClicked.clicked} data-value='japanese'>
                        <svg
                            className={`fill-black rounded-full max-sm:size-6 sm:size-7 md:size-8 lg:size-9 max-sm:mb-3 hover:bg-peach-400 ${(filterIsClicked.clicked && filterIsClicked.elementId === '4') ? 'bg-peach-400' : 'bg-peach-100'}`}
                            version='1.1'
                            xmlns='http://www.w3.org/2000/svg'
                            xmlnsXlink='http://www.w3.org/1999/xlink'
                            viewBox='0 0 24 24'
                            xmlSpace='preserve'
                        >
                            <g transform='scale(0.0808)'>
                            <path d='M290.784,147.476L133.921,84.731V58.807c0-24.929-55.779-26.191-66.96-26.191S0,33.879,0,58.807v97.846
                                c0,24.929,55.779,26.191,66.96,26.191c9.109,0,47.816-0.84,61.849-14.63l136.371,94.41c1.717,1.188,3.676,1.759,5.617,1.759
                                c3.134,0,6.216-1.487,8.135-4.258c3.107-4.488,1.988-10.645-2.5-13.752l-142.512-98.662v-41.69l149.523,59.809
                                c1.204,0.482,2.446,0.709,3.668,0.709c3.922,0,7.634-2.351,9.18-6.216C298.318,155.256,295.852,149.504,290.784,147.476z
                                M79.449,52.803c14.775,1.002,25.447,3.645,31.117,6.004c-5.669,2.359-16.342,5.002-31.117,6.004
                                c1.862-1.346,3.077-3.531,3.077-6.004S81.311,54.15,79.449,52.803z M54.471,52.803c-1.862,1.346-3.077,3.531-3.077,6.004
                                s1.216,4.658,3.077,6.004c-14.775-1.002-25.447-3.645-31.117-6.004C29.024,56.449,39.696,53.805,54.471,52.803z M64.432,99.605
                                c-4.489-3.106-10.644-1.987-13.752,2.5c-3.107,4.488-1.988,10.645,2.5,13.752l58.331,40.383c-6.712,3.065-22.224,6.839-44.551,6.839
                                c-26.091,0-42.886-5.154-47.193-8.307V78.595c17.201,5.937,40.408,6.403,47.193,6.403c6.786,0,29.993-0.466,47.193-6.403v55.431
                                L64.432,99.605z'/>
                            </g>
                        </svg>

                        <h2 className='max-sm:text-sm sm:text-sm lg:text-md font-quicksand'>
                            Japanese
                        </h2>
                    </button>

                    {/* Filter 2 */}

                    <button id='1' className='flex flex-col justify-between items-center pt-3' onClick={handleFilterChange} disabled={filterIsClicked.elementId !== '1' && filterIsClicked.clicked} data-value='italian'>
                        <svg className={`fill-none rounded-full max-sm:size-6 sm:size-7 md:size-8 lg:size-9 max-sm:mb-3 hover:bg-peach-400 ${(filterIsClicked.clicked && filterIsClicked.elementId === '1') ? 'bg-peach-400' : 'bg-peach-100'}`}
                            viewBox='0 -0.5 25 25'
                            xmlns='http://www.w3.org/2000/svg'>
                            <path fill-rule='evenodd' clip-rule='evenodd' d='M18.284 10.459C19.3566 12.615 19.7406 15.0485 19.384 17.43L16.86 17.53L5.5 18L11.576 8.182L12.926 6C15.1365 6.88592 17.0113 8.44622 18.284 10.459Z' stroke='#000000' stroke-width='1.5' stroke-linecap='round' stroke-linejoin='round'/>
                            <path d='M16.86 17.533C16.916 15.5996 16.446 13.6872 15.5 12C14.5868 10.3761 13.2243 9.05037 11.576 8.18201' stroke='#000000' stroke-width='1.5' stroke-linecap='round' stroke-linejoin='round'/>
                            <path d='M14.27 15.23C13.9943 15.23 13.77 15.0057 13.77 14.73C13.77 14.4543 13.9943 14.23 14.27 14.23C14.5457 14.23 14.77 14.4543 14.77 14.73C14.77 15.0057 14.5457 15.23 14.27 15.23Z' fill='#000000'/>
                            <path d='M14.27 13.73C14.8223 13.73 15.27 14.1777 15.27 14.73C15.27 15.2823 14.8223 15.73 14.27 15.73C13.7177 15.73 13.27 15.2823 13.27 14.73C13.27 14.1777 13.7177 13.73 14.27 13.73Z' fill='#707070'/>
                            <path d='M9.78601 15.799C9.51031 15.799 9.28601 15.5747 9.28601 15.299C9.28601 15.0233 9.51031 14.799 9.78601 14.799C10.0617 14.799 10.286 15.0233 10.286 15.299C10.286 15.5747 10.0617 15.799 9.78601 15.799Z' fill='#000000'/>
                            <path d='M9.78601 14.299C10.3383 14.299 10.786 14.7467 10.786 15.299C10.786 15.8513 10.3383 16.299 9.78601 16.299C9.23373 16.299 8.78601 15.8513 8.78601 15.299C8.78601 14.7467 9.23373 14.299 9.78601 14.299Z' fill='#707070'/>
                            <path d='M12.27 12.23C11.9943 12.23 11.77 12.0057 11.77 11.73C11.77 11.4543 11.9943 11.23 12.27 11.23C12.5457 11.23 12.77 11.4543 12.77 11.73C12.77 12.0057 12.5457 12.23 12.27 12.23Z' fill='#000000'/>
                            <path d='M12.27 10.73C12.8223 10.73 13.27 11.1777 13.27 11.73C13.27 12.2823 12.8223 12.73 12.27 12.73C11.7177 12.73 11.27 12.2823 11.27 11.73C11.27 11.1777 11.7177 10.73 12.27 10.73Z' fill='#707070'/>
                        </svg>
                        <h2 className='max-sm:text-sm sm:text-sm lg:text-md font-quicksand'>
                            Italian
                        </h2>
                    </button>

                    {/* Filter 3 */}

                    <button id='12' className='flex flex-col justify-between items-center pt-3' onClick={handleFilterChange} disabled={filterIsClicked.elementId !== '12' && filterIsClicked.clicked} data-value='seafood'>
                        <svg 
                            className={`fill-black rounded-full max-sm:size-6 sm:size-7 md:size-8 lg:size-9 max-sm:mb-3 hover:bg-peach-400 ${(filterIsClicked.clicked && filterIsClicked.elementId === '12') ? 'bg-peach-400' : 'bg-peach-100'}`}
                            version='1.1' id='Layer_1' 
                            xmlns='http://www.w3.org/2000/svg' 
                            xmlns:xlink='http://www.w3.org/1999/xlink' 
                            viewBox='0 0 511.997 511.997' xml:space='preserve'>
                            <g>
                                <g>
                                    <path d='M435.626,98.711C435.626,44.282,391.344,0,336.915,0h-1.587c-4.551,0-8.241,3.69-8.241,8.241v90.47
                                        c0,29.924,24.346,54.27,54.27,54.27c4.208,0,8.301-0.498,12.235-1.409c4.941,32.741,4.461,56.354,3.919,66.021
                                        c-3.769,1.434-9.716,3.617-17.413,6.2c-9.831-24.894-21.615-45.705-32.489-61.972c-27.03-40.437-53.847-61.895-54.977-62.789
                                        c-3.28-2.598-7.985-2.325-10.943,0.633l-25.692,25.692l-25.692-25.692c-2.958-2.958-7.665-3.231-10.943-0.633
                                        c-1.129,0.893-27.947,22.352-54.977,62.789c-10.873,16.266-22.657,37.076-32.488,61.967c-7.701-2.583-13.651-4.767-17.422-6.2
                                        c-0.551-9.642-1.038-33.164,3.926-66.017c3.936,0.911,8.028,1.409,12.238,1.409c29.924,0,54.27-24.346,54.27-54.27V8.241
                                        c0-4.551-3.69-8.241-8.241-8.241h-1.587c-54.429,0-98.711,44.282-98.711,98.711c0,19.717,10.572,37.009,26.343,46.512
                                        c-7.679,47.818-4.391,77.798-4.242,79.099c0.343,2.975,2.271,5.529,5.039,6.673c0.757,0.313,9.005,3.692,22.723,8.313
                                        c-9.741,28.929-16.478,62.6-16.478,100.446c0,4.551,3.69,8.241,8.241,8.241c4.551,0,8.241-3.69,8.241-8.241
                                        c0-35.958,6.427-67.946,15.697-95.41c15.275,4.668,34.336,9.828,55.805,14.216c-0.138,5.411,0.353,10.249,1.48,14.502v37.7
                                        l-20.282-11.916c-3.235-1.901-7.349-1.375-10.002,1.279l-15.987,15.986c-3.219,3.219-3.219,8.437,0,11.655
                                        c3.217,3.218,8.436,3.218,11.654,0l11.481-11.479l23.136,13.591v20.144l-20.282-11.916c-3.235-1.901-7.349-1.375-10.002,1.279
                                        l-15.987,15.986c-3.219,3.218-3.219,8.437,0,11.655c3.217,3.219,8.436,3.219,11.654,0l11.481-11.479l23.136,13.591v20.144
                                        l-20.282-11.916c-3.235-1.901-7.349-1.376-10.002,1.279l-15.987,15.986c-3.219,3.218-3.219,8.437,0,11.655
                                        c3.217,3.218,8.436,3.218,11.654,0l11.481-11.479l23.136,13.591v14.525h-1.375c-11.892,0-23.075,4.635-31.491,13.051
                                        c-8.416,8.416-13.051,19.6-13.051,31.491c0,24.555,19.982,44.53,44.543,44.53h116.308c24.561,0,44.542-19.976,44.542-44.53
                                        c0-11.893-4.635-23.075-13.051-31.491c-8.416-8.416-19.6-13.051-31.491-13.051h-1.376v-14.525l23.137-13.593l11.479,11.479
                                        c3.217,3.218,8.436,3.218,11.654,0c3.218-3.218,3.218-8.436,0-11.654l-15.986-15.987c-2.655-2.655-6.767-3.18-10.002-1.279
                                        l-20.283,11.916v-20.144l23.137-13.593l11.479,11.479c3.217,3.218,8.436,3.218,11.654,0c3.218-3.218,3.218-8.436,0-11.654
                                        l-15.986-15.987c-2.655-2.655-6.767-3.18-10.002-1.279L312.78,350.02v-20.144l23.137-13.591l11.479,11.479
                                        c3.217,3.219,8.436,3.219,11.654,0c3.218-3.218,3.218-8.436,0-11.654l-15.986-15.987c-2.655-2.655-6.767-3.18-10.002-1.279
                                        L312.78,310.76v-24.183v-13.513c1.128-4.255,1.62-9.094,1.481-14.506c21.47-4.388,40.533-9.549,55.807-14.217
                                        c9.274,27.476,15.695,59.462,15.695,95.411c0,4.551,3.69,8.241,8.241,8.241c4.551,0,8.241-3.69,8.241-8.241
                                        c0-37.846-6.737-71.517-16.478-100.446c13.718-4.621,21.966-8,22.723-8.313c2.768-1.144,4.696-3.699,5.039-6.673
                                        c0.151-1.302,3.437-31.283-4.241-79.099C425.054,135.72,435.626,118.429,435.626,98.711z M168.426,98.712
                                        c0,18.004-12.662,33.099-29.546,36.871v-11.742c0-20.389,11.733-38.394,29.546-46.85V98.712z M92.851,98.712
                                        c0-43.104,33.332-78.566,75.575-81.963v42.473c-27.402,9.432-46.029,35.132-46.029,64.619v11.742
                                        C105.513,131.811,92.851,116.717,92.851,98.712z M256.044,152.975c22.301,38.077,48.667,95.424,40.207,117.726
                                        c-4.395,11.589-10.342,18.875-17.673,21.654c-9.225,3.5-18.411-1.145-18.685-1.287c-2.45-1.315-5.401-1.306-7.843,0.026
                                        c-0.09,0.048-9.17,4.763-18.49,1.314c-7.397-2.737-13.393-10.041-17.818-21.708c-2.877-7.584-4.325-27.531,18.301-75.906
                                        C241.838,178.13,250.298,162.873,256.044,152.975z M279.064,378.283h-46.133c-9.5,0-17.23-7.729-17.23-17.23v-13.668
                                        c5.046,3.013,10.938,4.75,17.23,4.75h46.133c6.292,0,12.183-1.737,17.229-4.75v13.679
                                        C296.287,370.56,288.562,378.283,279.064,378.283z M296.293,390.016v13.679c-0.006,9.495-7.731,17.219-17.229,17.219h-46.133
                                        c-9.5,0-17.23-7.729-17.23-17.23v-13.668c5.046,3.013,10.938,4.75,17.23,4.75h46.133
                                        C285.356,394.766,291.247,393.029,296.293,390.016z M279.064,335.653h-46.133c-9.5,0-17.23-7.729-17.23-17.23v-17.925
                                        c3.66,3.263,7.708,5.73,12.139,7.37c11.606,4.295,22.526,1.542,28.156-0.565c3.457,1.293,8.906,2.831,15.287,2.831
                                        c4.01,0,8.39-0.608,12.87-2.266c4.43-1.639,8.479-4.106,12.139-7.37v17.936h0.001
                                        C296.287,327.929,288.562,335.653,279.064,335.653z M147.645,228.82c9.161-22.959,20.04-42.213,30.099-57.323
                                        c18.558-27.88,37.321-46.519,46.338-54.744l21.499,21.499c-5.412,9.021-16.304,27.855-26.398,49.413
                                        c-9.917,21.181-16.4,39.373-19.445,54.481C179.903,238.037,162.09,233.215,147.645,228.82z M226.851,495.518h-29.007
                                        c-15.474,0-28.061-12.583-28.061-28.048c0-7.489,2.921-14.534,8.224-19.837c5.303-5.303,12.348-8.224,19.837-8.224h1.411
                                        c0.368,12.618,6.897,24.058,17.657,30.763c0.079,0.049,0.16,0.098,0.241,0.144c6.078,3.894,9.698,10.51,9.698,17.752V495.518z
                                        M268.662,488.066v7.45h-12.664v0.001h-12.664v-7.45c0-13.061-6.614-24.98-17.692-31.884c-0.079-0.049-0.16-0.098-0.241-0.144
                                        c-6.079-3.894-9.699-10.51-9.699-17.752v-5.643c5.046,3.013,10.938,4.75,17.23,4.75h46.133c6.292,0,12.183-1.737,17.229-4.75
                                        v5.641c0,7.241-3.62,13.856-9.697,17.75c-0.081,0.047-0.161,0.096-0.242,0.145C275.276,463.087,268.662,475.006,268.662,488.066z
                                        M314.151,439.409c7.489,0,14.534,2.921,19.837,8.224c5.303,5.303,8.223,12.348,8.223,19.837c0,15.466-12.588,28.048-28.06,28.048
                                        h-29.007v-7.45c0-7.24,3.621-13.856,9.698-17.75c0.081-0.047,0.162-0.096,0.242-0.145c10.759-6.705,17.288-18.145,17.656-30.763
                                        H314.151z M312.257,242.147c-3.045-15.108-9.529-33.3-19.445-54.481c-10.093-21.558-20.987-40.393-26.398-49.413l21.505-21.505
                                        c8.945,8.145,27.477,26.542,45.989,54.235c10.178,15.228,21.194,34.655,30.446,57.842
                                        C349.919,233.215,332.11,238.035,312.257,242.147z M373.114,135.584c-16.884-3.772-29.546-18.867-29.546-36.872V76.992
                                        c17.813,8.456,29.546,26.461,29.546,46.85V135.584z M389.598,135.584v-11.742c0-29.487-18.627-55.188-46.029-64.619V16.749
                                        c42.243,3.398,75.575,38.86,75.575,81.963C419.143,116.717,406.483,131.811,389.598,135.584z'/>
                                    </g>
                                </g>
                            <g>
                                <g>
                                    <circle cx='241.246' cy='209.195' r='8.241'/>
                                </g>
                            </g>
                            <g>
                                <g>
                                    <circle cx='270.761' cy='209.195' r='8.241'/>
                                </g>
                            </g>
                        </svg>
                        <h2 className='max-sm:text-sm sm:text-sm lg:text-md font-quicksand'>
                            Seafood
                        </h2>
                    </button>

                    {/* Filter 4 */}

                    <div id='filter-4' className='flex flex-col justify-between items-center pt-3' onClick={handleMoreIsClicked}>
                        <svg xmlns='http://www.w3.org/2000/svg' 
                            viewBox='0 0 24 24' stroke-width='1.5' 
                            stroke='currentColor' 
                            className={`fill-none rounded-full max-sm:size-6 sm:size-7 md:size-8 lg:size-9 max-sm:mb-3 hover:bg-peach-400 ${(filterIsClicked.clicked && filterIsClicked.elementId === 'filter-2') ? 'bg-peach-400' : 'bg-peach-100'}`}>
                            <path stroke-linecap='round' stroke-linejoin='round' d='M6.75 12a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0ZM12.75 12a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0ZM18.75 12a.75.75 0 1 1-1.5 0 .75.75 0 0 1 1.5 0Z' />
                        </svg>
                        <h2 className='max-sm:text-sm sm:text-sm lg:text-md font-quicksand'>
                            More
                        </h2>
                    </div>
                </div>
            </div>

        </section>
    );
}

export default SearchBar;