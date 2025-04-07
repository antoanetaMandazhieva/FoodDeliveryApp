import hosomaki from '../../assets/images/page_images/hosomaki_graphic.png';
import pizza from '../../assets/images/page_images/pizza_graphic.png';
import lobster from '../../assets/images/page_images/lobster_graphic.png';
import gsap from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import { useGSAP } from '@gsap/react';
import { useState, useRef } from 'react';

const Cuisines = () => {
    const [japIsHovered, setJapIsHovered] = useState(() => false);
    const [itaIsHovered, setItaIsHovered] = useState(() => false);
    const [seaIsHovered, setSeaIsHovered] = useState(() => false);

    const japImgRef = useRef(null);
    const itaImgRef = useRef(null);
    const seaImgRef = useRef(null);

    useGSAP(() => {
        gsap.registerPlugin(ScrollTrigger);

        gsap.fromTo('.cui', {
            x: 120,
            y: 100,
            opacity: 0
        }, {
            x: 0,
            y: 0,
            opacity: 1,
            duration: 1.2,
            stagger: 0.4,
            ease: 'power2.inOut',
            scrollTrigger: {
                trigger: '#cui-top',
                start: 'top top'
            }
        })
    }, [])

    useGSAP(() => {
        if (japIsHovered) {
            gsap.to(japImgRef.current, {
                scale: 0.9,
                duration: 0.3,
                ease: 'power1.inOut',
         
            });
        }
        else {
            gsap.set(japImgRef.current, {
                scale: 1
            })
        }
    }, [japIsHovered])

    useGSAP(() => {
        if (itaIsHovered) {
            gsap.to(itaImgRef.current, {
                scale: 0.9,
                duration: 0.3,
                ease: 'power1.inOut',
    
            });
        }
        else {
            gsap.set(itaImgRef.current, {
                scale: 1
            })
        }
    }, [itaIsHovered])

    useGSAP(() => {
        if (seaIsHovered) {
            gsap.to(seaImgRef.current, {
                scale: 0.9,
                duration: 0.3,
                ease: 'power1.inOut',
                
            });
        }
        else {
            gsap.to(seaImgRef.current, {
                scale: 1
            })
        }
    }, [seaIsHovered])
    
    return (
        <section className='max-md:h-[300px] md:h-[540px] lg:h-[600px] xl:h-[660px] w-full bg-ivory mt-0'>

            {/* top */}

            <div id='cui-top' className='h-[40%] w-full relative'>
                <svg
                    className='absolute'
                    viewBox='0 0 1440 227'
                    fill='none'
                    xmlns='http://www.w3.org/2000/svg'
                >
                    <g filter='url(#filter0_d_10_10)'>
                    <path
                        d='M0 0H1440V224C751 165.5 210 -186.5 0 224V0Z'
                        fill='url(#paint0_linear_10_10)'
                    />
                    </g>
                    <defs>
                    <filter
                        id='filter0_d_10_10'
                        x='-4'
                        y='0'
                        width='1448'
                        height='234'
                        filterUnits='userSpaceOnUse'
                        color-interpolation-filters='sRGB'
                    >
                        <feFlood flood-opacity='0' result='BackgroundImageFix' />
                        <feColorMatrix
                        in='SourceAlpha'
                        type='matrix'
                        values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0'
                        result='hardAlpha'
                        />
                        <feOffset dy='6' />
                        <feGaussianBlur stdDeviation='2' />
                        <feComposite in2='hardAlpha' operator='out' />
                        <feColorMatrix
                        type='matrix'
                        values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.25 0'
                        />
                        <feBlend
                        mode='normal'
                        in2='BackgroundImageFix'
                        result='effect1_dropShadow_10_10'
                        />
                        <feBlend
                        mode='normal'
                        in='SourceGraphic'
                        in2='effect1_dropShadow_10_10'
                        result='shape'
                        />
                    </filter>
                    <linearGradient
                        id='paint0_linear_10_10'
                        x1='0'
                        y1='113.724'
                        x2='1440'
                        y2='113.724'
                        gradientUnits='userSpaceOnUse'
                    >
                        <stop stop-color='#ECA447' />
                        <stop offset='0.260577' stop-color='#FDCE9A' />
                        <stop offset='0.9375' stop-color='#FCCD99' />
                    </linearGradient>
                    </defs>
                </svg>
                <h1 className='max-sm:text-2xl md:text-6xl xl:text-8xl font-quicksand
                        relative left-[6.5%] max-sm:top-9 md:top-18 lg:top-[108px] xl:top-36'>
                    Popular Cuisines
                </h1>
            </div>
            
            {/* bottom */}

            <div id='cui-bottom' className='max-md:h-[60%] md:h-[60%] lg:h-[65%] xl:h-[60%] w-full flex justify-around items-center bg-ivory relative'>
                {/* japanese */}
                <div className='relative cui'>
                    <div className='h-full w-full relative'>
                        <svg className='z-0 max-md:w-[140px] max-md:h-[93px] md:w-[200px] md:h-[153px] lg:w-[313px] lg:h-[266px]' viewBox='0 0 313 266' fill='none' xmlns='http://www.w3.org/2000/svg' onMouseEnter={() => setJapIsHovered(prev => !prev)} onMouseLeave={() => setJapIsHovered(prev => !prev)}>
                            <g filter='url(#filter0_d_14_13)'>
                                <path d='M309 99.3205C309 174.257 214.161 254 140.151 254C66.1411 254 4 217.133 4 142.197C4 67.2605 128.856 0 202.866 0C276.876 0 309 24.3844 309 99.3205Z' fill='#FFD69F'/>
                            </g>
                            <defs>
                                <filter id='filter0_d_14_13' x='0' y='0' width='313' height='266' filterUnits='userSpaceOnUse' color-interpolation-filters='sRGB'>
                                    <feFlood flood-opacity='0' result='BackgroundImageFix'/>
                                    <feColorMatrix in='SourceAlpha' type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0' result='hardAlpha'/>
                                    <feOffset dy='8'/>
                                    <feGaussianBlur stdDeviation='2'/>
                                    <feComposite in2='hardAlpha' operator='out'/>
                                    <feColorMatrix type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.25 0'/>
                                    <feBlend mode='normal' in2='BackgroundImageFix' result='effect1_dropShadow_14_13'/>
                                    <feBlend mode='normal' in='SourceGraphic' in2='effect1_dropShadow_14_13' result='shape'/>
                                </filter>
                            </defs>
                        </svg>
                    </div>   
                    <img src={hosomaki} className='absolute max-md:size-[58px] md:size-24 lg:size-40 
                        max-md:left-10 max-md:top-4 md:left-12 md:top-8 lg:left-18 lg:top-16 z-20'
                        ref={japImgRef}/>
                    <h1 className='max-md:text-md md:text-2xl lg:text-[32px] font-bold font-quicksand 
                        z-20 absolute max-md:left-8 md:left-10 lg:left-18 lg:top-64'>
                        Japanese
                    </h1>
                </div>
                {/* italian */}
                <div className='relative cui'>
                    <div className='h-full w-full relative'>
                        <svg className='z-0 max-md:w-[140px] max-md:h-[93px] md:w-[200px] md:h-[153px] lg:w-[313px] lg:h-[266px]' viewBox='0 0 313 266' fill='none' xmlns='http://www.w3.org/2000/svg' onMouseEnter={() => setItaIsHovered(prev => !prev)} onMouseLeave={() => setItaIsHovered(prev => !prev)}>
                            <g filter='url(#filter0_d_16_23)'>
                                <path d='M309 99.3205C309 174.257 214.161 254 140.151 254C66.1411 254 4 217.133 4 142.197C4 67.2605 128.856 0 202.866 0C276.876 0 309 24.3844 309 99.3205Z' fill='#FFD79C'/>
                            </g>
                            <defs>
                                <filter id='filter0_d_16_23' x='0' y='0' width='313' height='266' filterUnits='userSpaceOnUse' color-interpolation-filters='sRGB'>
                                    <feFlood flood-opacity='0' result='BackgroundImageFix'/>
                                    <feColorMatrix in='SourceAlpha' type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0' result='hardAlpha'/>
                                    <feOffset dy='8'/>
                                    <feGaussianBlur stdDeviation='2'/>
                                    <feComposite in2='hardAlpha' operator='out'/>
                                    <feColorMatrix type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.25 0'/>
                                    <feBlend mode='normal' in2='BackgroundImageFix' result='effect1_dropShadow_16_23'/>
                                    <feBlend mode='normal' in='SourceGraphic' in2='effect1_dropShadow_16_23' result='shape'/>
                                </filter>
                            </defs>
                        </svg>
                    </div>
                    <img src={pizza} className='absolute max-md:size-[58px] md:size-24 lg:size-40 
                        max-md:left-10 max-md:top-4 md:left-12 md:top-8 lg:left-18 lg:top-16 z-20'
                        ref={itaImgRef}/>
                    <h1 className='max-md:text-md md:text-2xl lg:text-[32px] font-bold font-quicksand z-20 
                        absolute max-md:left-[44px] md:left-14 lg:left-24 xl:left-[100px] lg:top-64'>
                        Italian
                    </h1>
                </div>
                {/* seafood */}
                <div className='relative cui'>
                    <div className='h-full w-full relative'>
                        <svg className='z-0 max-md:w-[140px] max-md:h-[93px] md:w-[200px] md:h-[153px] lg:w-[313px] lg:h-[266px]' viewBox='0 0 313 266' fill='none' xmlns='http://www.w3.org/2000/svg' onMouseEnter={() => setSeaIsHovered(prev => !prev)} onMouseLeave={() => setSeaIsHovered(prev => !prev)}>
                            <g filter='url(#filter0_d_16_25)'>
                                <path d='M309 99.3205C309 174.257 214.161 254 140.151 254C66.1411 254 4 217.133 4 142.197C4 67.2605 128.856 0 202.866 0C276.876 0 309 24.3844 309 99.3205Z' fill='#FFD69C'/>
                            </g>
                            <defs>
                                <filter id='filter0_d_16_25' x='0' y='0' width='313' height='266' filterUnits='userSpaceOnUse' color-interpolation-filters='sRGB'>
                                    <feFlood flood-opacity='0' result='BackgroundImageFix'/>
                                    <feColorMatrix in='SourceAlpha' type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 127 0' result='hardAlpha'/>
                                    <feOffset dy='8'/>
                                    <feGaussianBlur stdDeviation='2'/>
                                    <feComposite in2='hardAlpha' operator='out'/>
                                    <feColorMatrix type='matrix' values='0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0.25 0'/>
                                    <feBlend mode='normal' in2='BackgroundImageFix' result='effect1_dropShadow_16_25'/>
                                    <feBlend mode='normal' in='SourceGraphic' in2='effect1_dropShadow_16_25' result='shape'/>
                                </filter>
                            </defs>
                        </svg>
                    </div>
                    <img src={lobster} className='absolute max-md:size-[58px] md:size-24 lg:size-40 
                        max-md:left-10 max-md:top-4 md:left-12 md:top-8 lg:left-18 lg:top-16 z-20'
                        ref={seaImgRef}/>
                    <h1 className='max-md:text-md md:text-2xl lg:text-[32px] font-bold font-quicksand 
                        z-20 absolute max-md:left-8 md:left-10 lg:left-18 lg:top-64'>
                        Seafood
                    </h1>
                </div>
            </div>
        </section>
    );
};

export default Cuisines;