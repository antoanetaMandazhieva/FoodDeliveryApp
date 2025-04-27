import { useForm } from 'react-hook-form';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import Navigation from '../common/Navigation';

const CreateRestaurant = () => {
    const { employeeId } = useParams();
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm({
        defaultValues: {
            name: '',
            address: {
                street: '',
                city: '',
                postalCode: '',
                country: ''
            },
            cuisineIds: ''
    }});

    const onSubmit = async (formData) => {
        try {
            const formatedIds = formData.cuisineIds.split(', ').map(value => parseInt(value))
            formData = {
                ...formData,
                cuisineIds: formatedIds
            }
            const { data } = await axios.post(`http://localhost:8080/api/restaurants/create/${employeeId}`, formData, {
                headers: { 'Content-Type': 'application/json' }
            });

            console.log(data);
            toast.success('Created Restaurant!', { autoClose: 2000 })
        } catch (e) {
            console.error('There was an error submitting the form: ', e);
            toast.error(e.response.data.message, { autoClose: 2000 });
        }
    }

    return (
        <div className='h-screen w-full flex flex-col justify-center items-center bg-ivory relative overflow-scroll'>
            <Navigation />
            <ToastContainer />
            <form className='user-order' onSubmit={handleSubmit(onSubmit)}>

                <input className='form-input' type='text' placeholder='name' {
                    ...register('name', {
                    required: 'Restaurant name required', 
                    pattern: /^[\p{L}0-9 '&\-.,]+$/u
                    })}
                />

                {errors.name && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.name.message}</div>
                )}

                <div className='flex flex-col items-center justify-between h-[10rem] w-full'>
                    <div className='flex flex-col justify-between items-center w-full'>
                        <input className='form-input' 
                            type='text' placeholder='Street' {
                            ...register('address.street', {
                                required: 'Address Street required',
                                pattern: /^[A-Za-z0-9\s.,-]+$/
                            })}
                        />
                    </div>

                    <div className='flex flex-col justify-between items-center w-full'>
                        <input className='form-input' 
                            type='text' placeholder='City and Neighbourhood' {
                            ...register('address.city', {
                                required: 'Address City and Neighbourhood required',
                                pattern: /^[A-Z][a-z]+\s[A-Z][a-z]+$/
                                
                            })}
                        />
                    </div>

                    <div className='flex flex-col justify-between items-center w-full'>
                        <input className='form-input' 
                            type='text' placeholder='Postal Code' {
                            ...register('address.postalCode', {
                                required: 'Address Postal Code required',
                                pattern: /^[0-9]{4}$/
                            })}
                        />
                    </div>

                    <div className='flex flex-col justify-between items-center w-full'>
                        <input className='form-input' 
                            type='text' placeholder='Country' {
                            ...register('address.country', {
                                required: 'Country required',
                                pattern: /^[A-Za-z]+$/
                            })}
                        />
                    </div>
                </div>

                <input className='form-input' type='text' placeholder='cuisineIds' {
                    ...register('cuisineIds', {
                    required: 'Cuisine IDs required', 
                    pattern: /^(?:[1-9]\d*)(?:, [1-9]\d*)*$/
                    })}
                />

                {errors.cuisineIds && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.cuisineIds.message}</div>
                )}
                
                <button className='bg-peach-400 hover:bg-peach-100 rounded-xl h-14 w-[30%]' disabled={isSubmitting}>
                    {isSubmitting ? 'Loading...' : 'Submit'}
                </button>
            </form>
        </div>
    );
}

export default CreateRestaurant;