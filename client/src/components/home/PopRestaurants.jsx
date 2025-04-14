import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import { ScrollTrigger } from 'gsap/ScrollTrigger';

const PopRestaurants = () => {
    useGSAP(() => {
        gsap.registerPlugin(ScrollTrigger); 

        gsap.fromTo('.item', {
            x: -80,
            y: 80,
            opacity: 0
        }, {
            x: 0,
            y: 0,
            opacity: 1,
            duration: 1.2,
            stagger: 0.2,
            ease: 'power2.inOut',
            scrollTrigger: {
                trigger: '#pop-rest-top',
                start: 'top top'
            }
        })
    }, [])

    return (
        <section className='w-full bg-ivory mt-0 max-md:pb-14 md:pb-20 lg:pb-28 xl:pb-32'>

            {/* top */}

            <div id='pop-rest-top' className='aspect-1440/528 bg-ivory relative'>
                <svg className='absolute' viewBox='0 0 1440 528' fill='none' xmlns='http://www.w3.org/2000/svg'>
                    <path d='M0 0L1440 135.5C1440 135.5 1440 758.111 1440 433.5C1440 108.889 0 159.5 0 159.5C6.02607e-06 395.865 0 0 0 0Z' fill='#FFD79C'/>
                </svg>
                <h1 className='max-sm:text-2xl sm:text-3xl md:text-6xl xl:text-8xl font-quicksand
                        relative left-[2%] max-sm:top-[5.5rem] sm:top-[7rem] md:top-[10rem] lg:top-[15rem] xl:top-[18rem]'>
                    Restaurants you might like
                </h1>
            </div>

            {/* bottom */}

            <div id='pop-rest-bottom' className='h-full grid grid-rows-2 grid-cols-3 gap-x-4 gap-y-10 px-4'>
                {/* placeholders */}
                <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                    <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
                </svg>
                <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                    <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
                </svg>
                <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                    <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
                </svg>
                <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                    <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
                </svg>
                <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                    <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
                </svg>
                <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                    <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
                </svg>
            </div>
        </section>

    );
}

export default PopRestaurants;