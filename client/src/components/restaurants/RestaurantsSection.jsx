import { useState } from 'react';
import sushiImg from '../../assets/images/page_images/sushi_plate.jpg'
import RatingStars from './RatingStars';
import { Link } from 'react-router-dom';

// Lines 8 - 44 DEMO OF DYNAMIC RENDERING

const RestaurantsSection = () => {
    const count = 23;
    const restId = 1;

    return (
        <section className={`grid md:grid-rows-${Math.ceil(count / 2)} 
            lg:grid-rows-${Math.ceil(count / 3)} md:grid-cols-2 lg:grid-cols-3
            sm:gap-x-3 lg:gap-x-6 max-sm:gap-y-4 sm:gap-y-6 lg:gap-y-12 bg-ivory px-6 py-12
            overflow-y-scroll`}
        >
            {/* DEMO */}
            <Link to={`/restaurants/${restId}`}>
                <div className='h-full w-full flex flex-col justify-between'>
                    <img src={sushiImg} 
                        className='aspect-406/251 rounded-4xl' 
                        alt='sushi-image'
                    />
                    <div className='flex justify-between items-center mt-2'>
                        <h1 className='text-3xl font-quicksand font-bold'>
                            Miyabi
                        </h1>
                        <div className='bg-peach-100 opacity-90 p-1 rounded-xl'>
                            <h2 className='text-md text-zinc-800 font-bold font-playfair'>
                                Japanese
                            </h2>
                        </div>
                    </div>
                    <div className='flex justify-between items-center mt-2'>
                        <h1 className='text-xl font-quicksand'>
                            Rating:
                        </h1>
                        <div className='flex justify-between items-center'>
                            <h2 className='font-quicksand text-lg'>
                                4.4
                            </h2>
                            <RatingStars rating={4.4}/>
                        </div>
                    </div>
                </div>
            </Link>


            <div className='flex flex-col justify-between'>
                <img src={sushiImg} 
                    className='aspect-406/251 rounded-4xl' 
                    alt='sushi-image'
                />
                <div className='flex justify-between items-center mt-2'>
                    <h1 className='text-3xl font-quicksand font-bold'>
                        McDonald's
                    </h1>
                    <div className='bg-peach-100 opacity-90 p-1 rounded-xl'>
                        <h2 className='text-md text-zinc-800 font-bold font-playfair'>
                            American
                        </h2>
                    </div>
                </div>
                <div className='flex justify-between items-center mt-2'>
                    <h1 className='text-xl font-quicksand'>
                        Rating:
                    </h1>
                    <div className='flex justify-between items-center'>
                        <h2 className='font-quicksand text-lg'>
                            4.9
                        </h2>
                        <RatingStars rating={4.6}/>
                    </div>
                </div>
            </div>
            <div className='flex flex-col justify-between'>
                <img src={sushiImg} 
                    className='aspect-406/251 rounded-4xl' 
                    alt='sushi-image'
                />
                <div className='flex justify-between items-center mt-2'>
                    <h1 className='text-3xl font-quicksand font-bold'>
                        Ugo
                    </h1>
                    <div className='bg-peach-100 opacity-90 p-1 rounded-xl'>
                        <h2 className='text-md text-zinc-800 font-bold font-playfair'>
                            Italian
                        </h2>
                    </div>
                </div>
                <div className='flex justify-between items-center mt-2'>
                    <h1 className='text-xl font-quicksand'>
                        Rating:
                    </h1>
                    <div className='flex justify-between items-center'>
                        <h2 className='font-quicksand text-lg'>
                            2.8
                        </h2>
                        <RatingStars rating={2.8}/>
                    </div>
                </div>
            </div>
            

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
            <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
            </svg>
            <svg className='aspect-406/251 item' viewBox='0 0 406 251' fill='none' xmlns='http://www.w3.org/2000/svg'>
                <rect width='406' height='251' rx='56' fill='#D9D9D9'/>
            </svg>
        </section>
    );
}

export default RestaurantsSection;