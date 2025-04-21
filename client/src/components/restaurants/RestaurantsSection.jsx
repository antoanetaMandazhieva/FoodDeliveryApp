import { useState } from 'react';
import RatingStars from './RatingStars';
import { Link } from 'react-router-dom';
import { getRestaurantLogoUrl } from '../../util/getImageURL';

const RestaurantsSection = ({ restaurants }) => {
    
    return (
        <section className={`grid md:grid-rows-${Math.ceil(restaurants.length / 2)} 
            lg:grid-rows-${Math.ceil(restaurants.length / 3)} 
            md:grid-cols-2 lg:grid-cols-3 sm:gap-x-3 lg:gap-x-6 max-sm:gap-y-4 
            sm:gap-y-6 lg:gap-y-12 bg-ivory px-6 py-12
            overflow-y-scroll`}
        >
            {restaurants.map(restaurant => <Link key={restaurant.id} to={`/restaurants/${restaurant.name}`}>
                    <div className='h-full w-full flex flex-col justify-between'>
                        <img 
                            src={getRestaurantLogoUrl('restaurant_images', `logo_${restaurant.id}.png`)} 
                            className='aspect-406/251 rounded-4xl' 
                            alt={`${restaurant.name}_image`}
                        />
                        <div className='flex justify-between items-center mt-2'>
                            <h1 className='text-3xl font-quicksand font-bold'>
                                {restaurant.name}
                            </h1>
                            <div className='flex justify-around items-center'>
                                {restaurant.cuisineDtos.map(cuisine => (
                                    <h2 key={cuisine.id} 
                                        className='bg-peach-100 opacity-90 p-1 rounded-xl text-sm text-zinc-800 font-bold font-playfair mx-1'>
                                        
                                        {cuisine.name}
                                    </h2>
                                ))}
                            </div>
                        </div>
                        <div className='flex justify-between items-center mt-2'>
                            <h1 className='text-xl font-quicksand'>
                                Rating:
                            </h1>
                            <div className='flex justify-between items-center'>
                                <h2 className='font-quicksand text-lg'>
                                    {restaurant.averageRating}
                                </h2>
                                <RatingStars rating={restaurant.averageRating}/>
                            </div>
                        </div>
                    </div>
                </Link>
            )}
        </section>
    );
}

export default RestaurantsSection;