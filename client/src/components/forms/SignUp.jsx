import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { setCookie } from '../../util/cookies';

const SignUp = () => {
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm({
        defaultValues: {
            name: '',
            surname: '',
            username: '',
            email: '',
            password: '',
            dateOfBirth: '',
            phoneNumber: '',
            gender: '',
            address: {
                street: '',
                city: '',
                postalCode: '',
                country: ''
            } 
        }
    });
    const navigate = useNavigate();
    
    const onSubmit = async (formData) => {
        console.log(formData)
        try {
            const res = await axios.post('http://127.0.0.1:8080/api/auth/register', formData, {
                headers: { 'Content-Type': 'application/json' }
            });

            setCookie('userRole', res.data.role, 1);
            setCookie('username', res.data.username, 1);

            confirm(res.data.message);
            navigate('/');
        } catch (error) {
            console.error('There was an error submitting the form: ', error);
            alert('Error submitting form. Please try again.');
        }
    }

    return (
        <div className='min-h-screen w-full flex justify-center p-5 bg-ivory relative overflow-hidden'>
            <form className='min-h-[80%] w-[80%] form p-6 rounded-4xl
                flex flex-col justify-between items-center' onSubmit={handleSubmit(onSubmit)}>

                <input className='form-input' type='text' placeholder='Name' {
                    ...register('name', {
                    required: 'Name required',
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

                <input className='form-input' type='text' placeholder='Surname' {
                    ...register('surname', {
                    required: 'Name required',
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

                <input className='form-input' type='text' placeholder='Username' {
                    ...register('username', {
                    required: 'Username required', 
                    minLength: {
                        value: 3,
                        message: 'Username length must be greater than 3'
                    }, 
                    maxLength: {
                        value: 16,
                        message: 'Username length must be less than 17'
                    }, 
                    pattern: /[A-Za-z0-9]+/
                    })}
                />

                {errors.username && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.username.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Email' {
                    ...register('email', { 
                    required: 'Email required',
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

                <input className='form-input' type='password' placeholder='Password' 
                    {...register('password', {
                    required: 'Requirements: one capital letter, one lower case letter, one digit, one special symbol',
                    minLength: {
                        value: 8,
                        message: 'Password length must be greater than 7'
                    },
                    maxLength: {
                        value: 16,
                        message: 'Password length must be less than 17'
                    },
                    pattern: {
                        value: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/,
                        message: 'Invalid password format'
                    },
                    })}
                />

                {errors.password && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.password.message}</div>
                )}

                <label id='birthdate' htmlFor='birthdate'>
                    Birthdate:
                </label>
                
                <input id='birthdate' type='date' className='form-input' {
                    ...register('dateOfBirth', {
                    required: 'Birthdate required',
                    min: {
                        value: '1900-01-01',
                        message: 'Invalid date'
                    },
                    max: {
                        value: '2025-04-14',
                        message: 'Invalid date'
                    }
                    })}
                />

                {errors.dateOfBirth && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.dateOfBirth.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Phone Number' {
                    ...register('phoneNumber', { 
                    required: 'Phone Number required',
                    pattern: {
                        value: /^(?:\+359|0)\d{9}$/,
                        message: 'Invalid phone number format'
                    }
                    })}
                />

                {errors.phoneNumber && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.phoneNumber.message}</div>
                )}

                <select className='form-input' {...register('gender', { required: true })}>
                    <option value='MALE'>Male</option>
                    <option value='FEMALE'> Female</option>
                </select>
                
                <div className='flex flex-col items-center justify-between md:grid md:grid-rows-2 md:grid-cols-2 gap-y-3 w-full'>
                    <div className='flex flex-col justify-between items-center'>
                        <input className='rounded-4xl text-center font-quicksand 
                            bg-peach-400 h-8 focus:outline 
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
                            bg-peach-400 h-8 focus:outline 
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
                            bg-peach-400 h-8 focus:outline 
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
                            bg-peach-400 h-8 focus:outline 
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
                </div>
                
                <button className='bg-peach-400 hover:bg-peach-100 rounded-xl h-14 w-[30%]' disabled={isSubmitting}>
                    {isSubmitting ? 'Loading...' : 'Submit'}
                </button>
            </form>
        </div>
    );
}

export default SignUp;