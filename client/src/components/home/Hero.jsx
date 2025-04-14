import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import sushi_plate from '../../assets/images/page_images/sushi_plate.jpg';
import heroImg from '../../assets/images/page_images/hero_image_2.avif'

const Hero = () => {
    useGSAP(() => {
        gsap.fromTo('#sushi-plate', {
            opacity: 0,
            scale: 0.1
        }, {
            opacity: 1,
            scale: 1,
            duration: 1.3,
            delay: 0.5,
            ease: 'power2.inOut'
        })
    }, [])

    return (
        <section className='relative aspect-[1439/687] w-full bg-ivory pb-0 mb-0'>
            <svg
                preserveAspectRatio='none'
                className='h-full w-full absolute z-0 bottom-0'
                viewBox='0 0 1439 687'
                fill='none'
                xmlns='http://www.w3.org/2000/svg'
            >
                <path
                d='M0 0H1141.81L1438.5 687H0V0Z'
                fill='url(#paint0_linear_24_21)'
                />
                <defs>
                <linearGradient
                    id='paint0_linear_24_21'
                    x1='1293'
                    y1='323'
                    x2='-7.17574e-06'
                    y2='323'
                    gradientUnits='userSpaceOnUse'
                >
                    <stop stop-color='#FCCD99' />
                    <stop offset='0.686041' stop-color='#FDCE9A' />
                    <stop offset='1' stop-color='#ECA447' />
                </linearGradient>
                </defs>
            </svg>
            <h1 id='hh-1' className='max-sm:text-xl sm:text-3xl md:text-6xl xl:text-8xl z-10 relative pl-4 
                sm:left-[5%] left-[9%] max-sm:top-[5%] sm:top-[10%] md:top-[15%] lg:top-[2%] font-quicksand font-bold'>
                Zaexpress
            </h1>
            <h2 id='hh-2' className='max-sm:text-xl sm:text-3xl md:text-6xl xl:text-8xl z-10 relative pl-4 
                sm:left-[2%] left-[6%] max-sm:top-[5%] sm:top-[10%] md:top-[15%] lg:top-[4%] font-quicksand'>
                Deliveries
            </h2>
            {/* <img
                src={sushi_plate}
                id='sushi-plate'
                className='max-sm:w-[220px] max-sm:h-[167px] md:w-[400px] md:h-[317px] 
                    lg:w-[550px] lg:h-[387px] xl:w-[790px] xl:h-[527px] z-10 relative 
                    rotate-[12deg] 
                    max-sm:left-[40%] max-sm:bottom-[10%] md:bottom-[15%] md:left-[48%] 
                    lg:left-[45%] lg:bottom-[17%] 
                    rounded-[4.5rem] shadow-2xl shadow-black'
                alt='sushi-plate'
            /> */}
            <img src={heroImg}
                id='hero-image'
                className='relative z-10 rotate-[12deg] h-full w-auto
                    max-sm:left-[45%] max-sm:bottom-[24%] sm:left-[40%] 
                    sm:bottom-[17%] md:bottom-[24%] md:left-[48%] 
                    lg:left-[50%] lg:bottom-[18%] xl:bottom-[24%] shadow-2xl
                    shadow-black rounded-4xl'
                alt='hero-image'
            />
        </section>
    );
};

export default Hero;