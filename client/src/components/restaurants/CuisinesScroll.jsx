import lobsterImg from '../../assets/images/page_images/lobster_graphic.png'
import pizzaImg from '../../assets/images/page_images/pizza_graphic.png'
import hosomakiImg from '../../assets/images/page_images/hosomaki_graphic.png'
import heroImg from '../../assets/images/page_images/hero_image_2.avif'
import { useRef, useState, useEffect } from 'react'
import gsap from 'gsap'
import { useGSAP } from '@gsap/react'
import { Draggable } from 'gsap/Draggable'
import { getCuisineImageUrl } from '../../util/getImageURL'
import axios from 'axios'

// Line 63: take design for dynamic rendering

const CuisinesScroll = ({ filterIsClicked, handleFilterChange, handleMoreIsClicked, ref }) => {
    const [cuisines, setCuisines] = useState([]);

    const triggerRef = useRef(null);
    // TODO: RESTfully get full list of cuisines
    useEffect(() => {
        const handleCuisines = async () => {
            const { data } = await axios.get('http://localhost:8080/api/cuisines', {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            setCuisines(data);
        }

        handleCuisines();
    }, [])

    useGSAP(() => {
        gsap.registerPlugin(Draggable)
        
        Draggable.create(triggerRef.current, {
            type: 'x',
            bounds: document.getElementById('cui-cont'),
            inertia: true
        })

    }, [])

    return (
        <section ref={triggerRef} className='h-full w-[400vw] absolute flex flex-col
            justify-around bg-gradient-to-br from-zinc-900 to-neutral-800 py-5'>
            
            <svg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke-width='1.5' 
                className='size-12 stroke-ivory absolute left-8 top-8' 
                onClick={handleMoreIsClicked}>
                <path stroke-linecap='round' stroke-linejoin='round' d='M6 18 18 6M6 6l12 12' />
            </svg>

            {/* Big screen helper */}
            
            <div className='max-lg:hidden lg:grid lg:grid-rows-2 lg:grid-cols-6 
                lg:grid-flow-col gap-x-6 w-fit h-fit items-center'>
                <div className='grid-cols-subgrid row-span-2 place-self-end h-full'>
                    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' stroke-width='1.5' className='size-12 stroke-ivory'>
                        <path stroke-linecap='round' stroke-linejoin='round' d='M6.75 15.75 3 12m0 0 3.75-3.75M3 12h18' />
                    </svg>
                </div>
                <div className='grid-rows-subgrid row-span-2 flex flex-col justify-around items-center w-fit'>
                    <h1 className='text-3xl text-ivory font-playfair font-bold'>
                        Hold and Drag with your mouse
                    </h1>
                    <h2 className='text-2xl text-zinc-500 font-playfair '>
                        Or use the scroller below
                    </h2>
                </div>
                <div className='grid-cols-subgrid row-span-2 h-full'>
                    <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' stroke-width='1.5' className='size-12 stroke-ivory'>
                        <path stroke-linecap='round' stroke-linejoin='round' d='M17.25 8.25 21 12m0 0-3.75 3.75M21 12H3' />
                    </svg>
                </div>
            </div>

            {/* Cuisines container */}

            <div className='flex justify-around items-center'>
                {cuisines.map(cui => (
                    <div id={cui.id} className='h-full flex flex-col justify-between items-center' data-value={cui.name}>
                        <img src={getCuisineImageUrl('cuisine_images', `cuisine_${cui.id}.jpg`)} className='max-sm:size-14 sm:size-20 md:size-36 lg:size-48 rounded-4xl mb-2'/>
                        <h1 className='max-sm:text-sm sm:text-md md:text-xl lg:text-2xl text-ivory font-quicksand mb-6'>
                            {cui.name}
                        </h1>
                        <button className={`w-full max-sm:h-5 sm:h-6 md:h-12 lg:h-16 rounded-4xl
                        hover:bg-zinc-600 max-sm:text-md sm:text-lg text-black 
                        font-quicksand font-bold ${(filterIsClicked.clicked && filterIsClicked.elementId == cui.id) ? 'bg-emerald-500' : 'bg-ivory'}`}
                        onClick={handleFilterChange}
                        disabled={filterIsClicked.clicked && filterIsClicked.elementId != cui.id}
                        >
                            {(filterIsClicked.clicked && filterIsClicked.elementId == cui.id)
                                ? 'Selected' : 'Select'
                            }
                        </button>
                    </div>
                ))}
            </div> 
        </section>
    );
}

export default CuisinesScroll;