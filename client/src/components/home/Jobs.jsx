import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
import logo from '../../assets/images/page_images/main_logo_ivory.png';
import bikerImg from '../../assets/images/page_images/biker_2.avif';
import partnersImg from '../../assets/images/page_images/partners.avif';
import employeesImg from '../../assets/images/page_images/employees.avif';

const Jobs = () => {
    useGSAP(() => {
        gsap.registerPlugin(ScrollTrigger); 

        gsap.fromTo('.job', {
            opacity: 0
        }, {
            opacity: 1,
            duration: 2,
            stagger: 0.66,
            ease: 'power2.inOut',
            scrollTrigger: {
                trigger: '#jobs-top',
                start: 'top top'
            }
        })
    }, []);

    return (
        <section id='jobs-section' className='w-full bg-ivory'>

            {/* top */}

            <div id='jobs-top' className='aspect-1440/270 bg-ivory w-full relative'>
                <svg preserveAspectRatio='none' className='h-full w-full absolute z-0' viewBox="0 0 1440 270" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M1441 270C1394.95 270 1118.42 270 720.5 270C322.579 270 178.771 270 0 270C0 201.301 322.579 0 720.5 0C1118.42 0 1441 201.301 1441 270Z" fill="#7BAAA0"/>
                </svg>
                <div className='flex flex-col justify-center items-center pt-2'>
                    <img src={logo} className='max-md:hidden md:h-20 lg:h-36 xl:h-48 max-h-52
                        w-auto rounded-full shadow-lg shadow-black opacity-95 z-10 relative' 
                        alt='logo'
                    />
                    <h1 className='relative z-10 text-ivory max-md:text-lg md:text-xl lg:text-5xl xl:text-7xl font-bold 
                        font-quicksand max-md:top-8'>
                        We believe in equal opportunities
                    </h1>
                </div>
            </div>

            {/* bottom */}

            <div id='jobs-bottom' className='h-[500px] bg-gradient-to-b
            from-teal to-teal-700 grid grid-rows-4 grid-cols-3 grid-flow-col 
            gap-x-6 gap-y-2 pt-8'>
                
                {/* Job 1 */}
                
                <div className='job grid-rows-subgrid row-span-2 flex flex-col justify-center items-center'>
                    <img src={bikerImg} className='max-sm:h-1/2 h-full w-auto rounded-full pb-3' alt='biker-image'/>
                    <h2 className='max-sm:text-md max-md:text-lg text-2xl text-ivory font-quicksand font-bold'>
                        Courrier
                    </h2>
                </div>
                <div className='job grid-rows-subgrid row-span-2 flex justify-center items-center'>
                    <p className='max-sm:text-xs max-md:text-sm text-lg text-ivory text-center font-quicksand'>
                        Can ride a bike? Have a driver's license? That's all you need,
                        and all we need is YOU! Apply now for a job as a courrier
                        at Zaexpress Deliveries, join our community and get special
                        privileges.
                    </p>
                </div>

                {/* Job 2 */}

                <div className='job grid-rows-subgrid row-span-2 flex flex-col justify-center items-center'>
                    <img src={partnersImg} className='max-sm:h-1/2 h-full w-auto rounded-full pb-3' alt='biker-image'/>
                    <h2 className='max-sm:text-md max-md:text-lg text-2xl text-ivory font-quicksand font-bold'>
                        Partner
                    </h2>
                </div>
                <div className='job grid-rows-subgrid row-span-2 flex flex-col justify-center items-center'>
                    <p className='max-sm:text-xs max-md:text-sm text-lg text-ivory text-center font-quicksand'>
                        Unlock new opportunities today. 
                        As our trusted partner, you’ll gain access to cutting-edge
                        logistics solutions, and unwavering support tailored to
                        scale your business. 
                    </p>
                </div>

                {/* Job 3 */}

                <div className='job grid-rows-subgrid row-span-2 flex flex-col justify-center items-center'>
                    <img src={employeesImg} className='max-sm:h-1/2 h-full w-auto rounded-full pb-3' alt='biker-image'/>
                    <h2 className='max-sm:text-md max-md:text-lg text-2xl text-ivory font-quicksand font-bold'>
                        Employee
                    </h2>
                </div>
                <div className='job grid-rows-subgrid row-span-2 flex flex-col justify-center items-center'>
                    <p className='max-sm:text-xs max-md:text-sm text-lg text-ivory text-center font-quicksand'>
                        Ready to build a rewarding career? Join our team at 
                        Zaexpress where innovation meets opportunity. 
                        Step into a role that empowers you to make a real impact and 
                        grow alongside us.
                    </p>
                </div>
            </div>
        </section>
    );
}

export default Jobs;