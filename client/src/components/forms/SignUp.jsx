import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const SignUp = () => {
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm();
    
    const onSubmit = async (formData) => {
        try {
            const res = await axios.post('http://127.0.0.1:5000/register', formData, {
                headers: { 'Content-Type': 'application/json' }
            });
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

                <label id='birthdate' for='birthdate'>
                    Birthdate:
                </label>
                
                <input id='birthdate' type='date' className='form-input' {
                    ...register('birthdate', {
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

                {errors.birthdate && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.birthdate.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Phone Number' {
                    ...register('phonenumber', { 
                    required: 'Phone Number required',
                    pattern: {
                        value: /^(\\+359|0)\\d{9}$/,
                        message: 'Invalid phone number format'
                    }
                    })}
                />

                {errors.phonenumber && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.phonenumber.message}</div>
                )}

                <select className='form-input' {...register("Gender", { required: true })}>
                    <option value="Male">Male</option>
                    <option value=" Female"> Female</option>
                </select>
                
                <button className='bg-peach-400 hover:bg-peach-100 rounded-xl h-14 w-[30%]' disabled={isSubmitting}>
                    {isSubmitting ? 'Loading...' : 'Submit'}
                </button>
            </form>
        </div>
    );
}

export default SignUp;