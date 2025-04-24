import { set, useForm } from 'react-hook-form';
import Navigation from '../common/Navigation';
import { useNavigate, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';

const SupplierReviewForm = () => {
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm();
    const { userId } = useParams();

    const navigate = useNavigate();

    const onSubmit = async (formData) => {
        console.log(formData)
        if (formData.supplierUsername === '' || formData.rating === '') {
            alert('Must select restaurant and leave a rating')
        }
        else {
            try {
                formData = { ...formData, clientId: userId };
                const supplierUsername =  formData.supplierUsername;
                
                delete formData.supplierUsername;

                const { data } = await axios.get(`http://localhost:8080/api/users/supplier/${supplierUsername}`, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                const supplierId = data;
                
                const response = await axios.post(`http://localhost:8080/api/reviews/suppliers/${supplierId}/add?clientId=${formData.clientId}&rating=${formData.rating}${formData.comment && `&comment=${formData.comment}`}`, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                console.log(response.data);
                confirm('Successfully posted review');

                navigate(`/profile/${userId}`);
            }
            catch (e) {
                console.error(e);
                alert(e.message);
            }
        }
    }

    return (
        <div className='h-screen w-full flex flex-col justify-between items-center bg-ivory overflow-y-scroll'>
            <Navigation />
            <form className='user-order' onSubmit={handleSubmit(onSubmit)}>
                <input 
                    type='text'
                    className='form-input' 
                    {...register('supplierUsername', {required: 'Must enter courrier username'})}
                    placeholder='Courrier Username'
                />

                {errors.supplierUsername && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.supplierUsername.message}</div>
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

export default SupplierReviewForm;