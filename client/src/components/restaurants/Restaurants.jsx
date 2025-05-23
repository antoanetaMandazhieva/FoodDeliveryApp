import Navigation from '../common/Navigation';
import SearchBar from './SearchBar';
import CuisinesScroll from './CuisinesScroll';
import { useState, useRef, useEffect } from 'react';
import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import RestaurantsSection from './RestaurantsSection';
import Footer from '../common/Footer';
import axios from 'axios';

const Restaurants = () => {
    const [moreIsClicked, setMoreIsClicked] = useState(() => false);
    const [filterData, setFilterData] = useState({elementId: '', value: ''});
    const [filterIsClicked, setFilterIsClicked] = useState({elementId: '', clicked: false});
    const [sortData, setSortData] = useState({elementId: 'sort-2', value: 'sorted/asc'});
    const [sortIsClicked, setSortIsClicked] = useState({elementId: 'sort-2', clicked: true});
    const [searchData, setSearchData] = useState('');
    const [searchQuery, setSearchQuery] = useState('');
    const [restaurants, setRestaurants] = useState([]);

    const cuiScrollRef = useRef(null);
    const searchBarRef = useRef(null);
    const restSectionRef = useRef(null);

    console.log(restaurants)

    /*
    MANY ERRORS WITH FILTER AND SORT DATA
    */

    const getSortResults = async () => {
        try {
            const { data } = await axios.get(`http://127.0.0.1:8080/api/restaurants/${sortData.value}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            setRestaurants(data);
        } catch (error) {
            alert('Could not fetch restaurants')
        }
    }

    useEffect(() => {
        getSortResults();
    }, [sortData]);

    useEffect(() => {
        if (searchData) {
            setRestaurants(searchData);
        }
        else {
            getSortResults();
        }
    }, [searchData]);

    useEffect(() => {
        const getFilterData = async () => {
            if (filterData.elementId && filterData.elementId !== '') {
                try {
                    const { data } = await axios.get(`http://localhost:8080/api/restaurants/cuisine/${filterData.elementId}`, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });
                    const dataIds = data.map(restaurant => restaurant.id);
                    console.log(dataIds)
                    setRestaurants(prev => prev.filter(restaurant => dataIds.includes(restaurant.id)));
                } catch (e) {
                    console.warn(e.message);
                    alert('Could not fetch restaurants by cuisine')
                }
            }
            else {
                getSortResults();
            }
        }

        getFilterData();
    }, [filterData])

    useGSAP(() => {
        gsap.set(cuiScrollRef.current, {
            x: '100%',
            opacity: 0
        })
    }, []);

    useGSAP(() => {
        if (moreIsClicked) {
            gsap.set(searchBarRef.current, {
                opacity: 0.2,
                zIndex: 10
            });
            gsap.set(restSectionRef.current, {
                opacity: 0.2,
                zIndex: 10
            })

            gsap.to(cuiScrollRef.current, {
                opacity: 1,
                zIndex: 20,
                x: 0,
                ease: 'power3.inOut',
                delay: 0.1,
                duration: 1
            });
        }
        else {
            gsap.to(cuiScrollRef.current, {
                opacity: 0,
                zIndex: 10,
                x: '100%',
                duration: 0.7
            });

            gsap.to(searchBarRef.current, {
                opacity: 1,
                zIndex: 20,
                x: 0,
                ease: 'power3.inOut',
                duration: 1,
                delay: 0.3
            });
            gsap.to(restSectionRef.current, {
                opacity: 1,
                zIndex: 20,
                x: 0,
                ease: 'power3.inOut',
                duration: 1,
                delay: 0.3
            })
        }
    }, [moreIsClicked])

    const handleFilterChange = (event) => {
        let id;
        let value;

        if (event.currentTarget.id) {
            id = event.currentTarget.id;
            value = event.currentTarget.dataset.value;
        }
        else if (event.currentTarget.parentElement.id) {
            id = event.currentTarget.parentElement.id;
            value = event.currentTarget.parentElement.dataset.value;
        } else {
            console.warn('Error');
        }

        if (filterData.elementId === id) {
            setFilterData({elementId: '', value: ''});
            setFilterIsClicked({elementId: '', clicked: false})
        }
        else {
            if (filterIsClicked.elementId === '') {
                setFilterIsClicked({elementId: id, clicked: true});
            }
            else {
                setFilterIsClicked(prev => {
                    return {
                        ...prev,
                        clicked: false
                    }
                });
                setFilterIsClicked({elementId: id, clicked: true});
            }
            setFilterData({elementId: id, value: value});
        }
    }

    const handleSortChange = (event) => {
        const { id } = event.currentTarget;
        const { value } = event.currentTarget.dataset;

        if (sortData.elementId === id) {
            setSortData({elementId: '', value: ''});
            setSortIsClicked({elementId: '', clicked: false})
        }
        else {
            if (sortIsClicked.elementId === '') {
                setSortIsClicked({elementId: id, clicked: true});
            }
            else {
                setSortIsClicked(prev => {
                    return {
                        ...prev,
                        clicked: false
                    }
                });
                setSortIsClicked({elementId: id, clicked: true});
            }
            setSortData({elementId: id, value: value});

        }
    }
    console.log(filterIsClicked)
    return (
        <div className='h-screen bg-ivory'>
            <Navigation />
            <main className='flex-grow'>
                <div id='cui-cont' className='inset-0 z-10 absolute overflow-y-hidden lg:overflow-x-scroll' ref={cuiScrollRef}>
                    <CuisinesScroll
                        filterIsClicked={filterIsClicked}
                        handleFilterChange={handleFilterChange}
                        handleMoreIsClicked={() => setMoreIsClicked(prev => !prev)}
                        ref={cuiScrollRef}
                    />
                </div>
                <div className='w-full inset-0 z-20 sticky' ref={searchBarRef}>
                    <SearchBar 
                        sortIsClicked={sortIsClicked}
                        handleSortChange={handleSortChange}
                        filterIsClicked={filterIsClicked}
                        handleFilterChange={handleFilterChange}
                        moreIsClicked={moreIsClicked}
                        handleMoreIsClicked={() => setMoreIsClicked(prev => !prev)}
                        searchQuery={searchQuery}
                        setSearchQuery={setSearchQuery}
                        setSearchData={setSearchData}
                    />
                </div>
                <div className='w-full inset-0 z-20' ref={restSectionRef}>
                    <RestaurantsSection 
                        restaurants={restaurants}
                    />
                </div>
            </main>
            <Footer />
        </div>
    );
}

export default Restaurants;