import axios from 'axios';
import { useForm } from 'react-hook-form';
import { useEffect, useState } from 'react';
import { getCookie } from '../../util/cookies';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ProfileUpdateForm = ({ userDetails, setUserDetails, setDisplayForm }) => {
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm({
        defaultValues: {
            name: userDetails.name,
            surname: userDetails.surname,
            username: userDetails.username,
            email: userDetails.email,
            dateOfBirth: userDetails.dateOfBirth,
            phoneNumber: userDetails.phoneNumber,
            gender: userDetails.gender,
            address: {
                street: '',
                city: '',
                postalCode: '',
                country: ''
            } 
        }
    });
    const [addAddressClick, setAddAddressClick] = useState(false);
    const [userId, setUserId] = useState();

    useEffect(() => {
        setUserId(getCookie('userId'));
    }, []);
    
    const onSubmit = async (formData) => {
        let updatedFormData;
        if (formData.address.street === '') {
            updatedFormData = { ...formData, addresses: userDetails.addresses }
        }
        else {
            updatedFormData = { ...formData, addresses: [formData.address, ...userDetails.addresses] }
        }
        delete updatedFormData.address;

        try {
            const { data } = await axios.put(`http://localhost:8080/api/users/${userId}`, updatedFormData, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setUserDetails(data);

            toast.success('Updated User Details!', { autoClose: 2000 });
        }
        catch (e) {
            console.error(e.message);
            alert('Could not update user')
        }

    }

    return (
        <div className='h-[45rem] w-full md:w-[40%] max-md:mb-8 flex flex-col justify-between items-center bg-peach-400 rounded-4xl p-4'>
            <ToastContainer />
            <form className='user-order' onSubmit={handleSubmit(onSubmit)}>

                <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' type='text' placeholder={userDetails.name} {
                    ...register('name', {
                    maxLength: {
                        value: 16,
                        message: 'Name length must be less than 17'
                    }, 
                    pattern: /[A-Za-z]+/
                    })}
                />

                {errors.name && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.name.message}</div>
                )}

                <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' type='text' placeholder={userDetails.surname} {
                    ...register('surname', {
                    maxLength: {
                        value: 16,
                        message: 'Name length must be less than 17'
                    }, 
                    pattern: /[A-Za-z]+/
                    })}
                />

                {errors.surname && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.surname.message}</div>
                )}

                <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' type='text' placeholder={userDetails.email} {
                    ...register('email', { 
                    maxLength: {
                        value: 64,
                        message: 'Email length must be less than 65'
                    }, 
                    pattern: {
                        value: /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/,
                        message: 'Invalid email format'
                    }
                    })}
                />

                {errors.email && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.email.message}</div>
                )}
                
                <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' type='text' placeholder={userDetails.phoneNumber} {
                    ...register('phoneNumber', { 
                    pattern: {
                        value: /^(?:\+359|0)\d{9}$/,
                        message: 'Invalid phone number format'
                    }
                    })}
                />

                {errors.phoneNumber && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.phoneNumber.message}</div>
                )}

                <select className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' {...register('gender')}>
                    <option value='MALE'>Male</option>
                    <option value='FEMALE'> Female</option>
                </select>

                {addAddressClick && <div className='flex flex-col items-center justify-between h-[10rem]'>
                    <div className='flex flex-col justify-between items-center'>
                        <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' 
                            type='text' placeholder='Street' {
                            ...register('address.street', {
                                required: 'Address Street required',
                                pattern: /^[A-Za-z0-9\s.,-]+$/
                            })}
                        />

                        {/* {errors.address.street && (
                            <div className='text-red-600 font-semibold px-2 text-center'>{errors.address.street.message}</div>
                        )} */}
                    </div>

                    <div className='flex flex-col justify-between items-center'>
                        <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' 
                            type='text' placeholder='City and Neighbourhood' {
                            ...register('address.city', {
                                required: 'Address City and Neighbourhood required',
                                pattern: /^[A-Z][a-z]+\s[A-Z][a-z]+$/
                                
                            })}
                        />

                        {/* {errors.address.city && (
                            <div className='text-red-600 font-semibold px-2 text-center'>{errors.address.city.message}</div>
                        )} */}
                    </div>

                    <div className='flex flex-col justify-between items-center'>
                        <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' 
                            type='text' placeholder='Postal Code' {
                            ...register('address.postalCode', {
                                required: 'Address Postal Code required',
                                pattern: /^[0-9]{4}$/
                            })}
                        />

                        {/* {errors.address.postalCode && (
                            <div className='text-red-600 font-semibold px-2 text-center'>{errors.address.postalCode.message}</div>
                        )} */}
                    </div>

                    <div className='flex flex-col justify-between items-center'>
                        <input className='rounded-4xl text-center font-quicksand 
                            bg-ivory h-8 focus:outline 
                            focus:outline-black opacity-100' 
                            type='text' placeholder='Country' {
                            ...register('address.country', {
                                required: 'Country required',
                                pattern: /^[A-Za-z]+$/
                            })}
                        />

                        {/* {errors.address.country && (
                            <div className='text-red-600 font-semibold px-2 text-center'>{errors.address.country.message}</div>
                        )} */}
                    </div>
                </div>}
                <div className='flex justify-around items-center w-full'>
                    <button className='bg-teal text-ivory font-quicksand hover:bg-peach-100 rounded-xl h-14 w-[30%]' onClick={() => setAddAddressClick(prev => !prev)}>
                        {addAddressClick ? 'Close' : 'Add Address' }
                    </button>

                    <button className='bg-teal text-ivory font-quicksand hover:bg-peach-100 rounded-xl h-14 w-[30%]' onClick={handleSubmit} disabled={isSubmitting}>
                        {isSubmitting ? 'Loading...' : 'Submit'}
                    </button>

                    <button className='bg-teal text-ivory font-quicksand hover:bg-peach-100 rounded-xl h-14 w-[30%]' onClick={setDisplayForm}>
                        Leave
                    </button>
                </div>
            </form>
        </div>
    );
}

export default ProfileUpdateForm;