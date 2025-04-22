import { set, useForm } from 'react-hook-form';
import Navigation from '../common/Navigation';
import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';

const RestaurantReviewForm = () => {
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm();
    const { userId } = useParams();

    const [restaurants, setRestaurants] = useState([]);
    
    useEffect(() => {
        const handleRestaurants = async () => {
            const { data } = await axios.get('http://localhost:8080/api/restaurants/sorted/asc', {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            setRestaurants(data);
        }

        handleRestaurants();
    }, []);

    const onSubmit = async (formData) => {
        console.log(formData)
        if (formData.restaurantId === '' || formData.rating === '') {
            alert('Must select restaurant and leave a rating')
        }
        else {
            const restaurantId = formData.restaurantId;
            delete formData.restaurantId;

            formData = { ...formData, clientId: userId }

            try {
                const { data } = await axios.post(`http://localhost:8080/api/reviews/restaurants/${restaurantId}/add?clientId=${formData.clientId}&rating=${formData.rating}${formData.comment && `&comment=${formData.comment}`}`, {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    }
                );
                console.log(data);
                confirm('Successfully posted review');
            }
            catch (e) {
                console.warn(e.message);
                alert('Could not post review')
            }
        }
    }

    return (
        <div className='h-screen w-full flex flex-col justify-between items-center bg-ivory overflow-y-scroll'>
            <Navigation />
            <form className='user-order' onSubmit={handleSubmit(onSubmit)}>
                <select className='form-input' 
                    {...register('restaurantId', {
                        required: 'Select a restaurant'
                    })}
                >
                    <option value=''>
                        Select Restaurant
                    </option>
                    {restaurants.map(restaurant => (
                        <option value={restaurant.id}>
                            {restaurant.name}
                        </option>
                    ))}
                </select>

                {errors.restaurantId && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.restaurantId.message}</div>
                )}

                <select className='form-input' 
                    {...register('rating', {
                        required: 'Must leave a rating'
                    })}
                >
                    <option value=''>Rating</option>
                    <option value='1'>1 Star</option>
                    <option value='2'>2 Stars</option>
                    <option value='3'>3 Stars</option>
                    <option value='4'>4 Stars</option>
                    <option value='5'>5 Stars</option>
                </select>

                <textarea className='rounded-4xl text-center font-quicksand bg-peach-400 w-[60%] h-64
                    focus:outline focus:outline-black opacity-100'
                    {...register('comment')}
                    placeholder='Leave a comment here. Your feedback is greatly appreciated'
                >
                </textarea>

                <button className='bg-peach-400 hover:bg-peach-100 rounded-xl h-14 w-[30%]' disabled={isSubmitting}>
                    {isSubmitting ? 'Loading...' : 'Submit'}
                </button>
            </form>
        </div>
    );
}

export default RestaurantReviewForm;