import { useGSAP } from '@gsap/react';
import main_logo_ivory from '../../assets/images/page_images/main_logo_ivory.png';
import { useState, useEffect, useRef } from 'react';   
import { Link } from 'react-router-dom';  
import gsap from 'gsap'; 

const Navigation = () => {
    const [isSmall, setIsSmall] = useState(() => false);
    const navRef = useRef(null);
    const smallNavRef = useRef(null);

    // TODO: get user role
    useGSAP(() => {
        gsap.set(smallNavRef.current, {
            x: '-100%',
            zIndex: 1
        })
    }, []);

    useGSAP(() => {
        if (isSmall) {
            gsap.set(navRef.current, {
                opacity: 0
            })
            gsap.to(smallNavRef.current, {
                delay: -0.2,
                zIndex: 30,
                x: 0,
                duration: 1,
                ease: 'power2.inOut'
            });
        }
        else {
            gsap.to(smallNavRef.current, {
                zIndex: 1,
                x: '-100%',
                duration: 0.8,
                ease: 'power2.inOut'
            });
            gsap.set(navRef.current, {
                opacity: 1
            });
        }
    }, [isSmall]);

    const smallNavigation = () => {
        return (
            <div className='h-[100vh] w-full bg-ivory absolute p-4 z-10' ref={smallNavRef}>
                <svg xmlns='http://www.w3.org/2000/svg' fill='none' 
                    viewBox='0 0 24 24' stroke-width='1.5' stroke='currentColor' 
                    className='size-8 absolute right-4' onClick={() => setIsSmall(prev => !prev)}>
                    <path stroke-linecap='round' stroke-linejoin='round' d='M6 18 18 6M6 6l12 12' />
                </svg>
                {navElements}
            </div>
        );
    }

    // TODO: set the rest of the links
    const navElements = <div className={`${isSmall && 'inline-block'}`}>
        <ul className={`${isSmall ? 'max-sm:block' : 'max-sm:hidden'} flex items-center justify-evenly h-full mx-2 min-w-[60%]`}>
            <Link to='/restaurants'>
                <li className='text-black mx-6 md:text-md lg:text-lg hover:text-peach-400 hover:scale-110 font-playfair'>
                    Restaurants
                </li>
            </Link>
            <Link to='/profile'>
                <li className='text-black mx-6 md:text-md lg:text-lg hover:text-peach-400 hover:scale-110 font-playfair'>
                    User Profile
                </li>
            </Link>
            <Link to='/courrier'>
                <li className='text-black mx-6 md:text-md lg:text-lg hover:text-peach-400 hover:scale-110 font-playfair'>
                    Courrier
                </li>
            </Link>
            <Link to='/employee'>
                <li className='text-black mx-6 md:text-md lg:text-lg hover:text-peach-400 hover:scale-110 font-playfair'>
                    Employee
                </li>
            </Link>
            <Link to='/admin'>
                <li className='text-black mx-6 md:text-md lg:text-lg hover:text-peach-400 hover:scale-110 font-playfair'>
                    Admin Panel
                </li>
            </Link>
        </ul>
    </div>

    const signInButton = <div className='flex items-center h-full md:h-2/3 max-sm:w-[80px] md:w-[181px] lg:w-1/9 mr-4'>
        <Link to='/signin' className='h-3/5 w-full mr-1'>
            <button className='h-full w-full rounded-[3rem] bg-peach-400 text-black max-sm:text-sm lg:text-xl font-playfair font-bold hover:bg-peach-200 hover:h-[95%] hover:w-[95%]'>
                Sign In
            </button>
        </Link>
    </div>

    return (
        <div className='max-sm:h-[7%] md:h-[15%] lg:h-[20%] w-full'>
            {smallNavigation()}
            <nav className='h-full w-full flex justify-between items-center z-20' ref={navRef}>
                <div className='h-full w-auto flex justify-around items-center ml-2'>
                    <svg xmlns='http://www.w3.org/2000/svg' fill='none' 
                        viewBox='0 0 24 24' stroke-width='1.5' stroke='currentColor' 
                        className='size-6 md:hidden' onClick={() => setIsSmall(prev => !prev)}>
                        <path stroke-linecap='round' stroke-linejoin='round' d='M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5' />
                    </svg>
                    <img src={main_logo_ivory} alt='LogoIvory' className='h-full max-md:w-auto md:h-2/3 md:w-36 lg:w-auto'/>
                </div>
                {navElements}
                {signInButton}
            </nav>
        </div>
        
    );
}

export default Navigation;