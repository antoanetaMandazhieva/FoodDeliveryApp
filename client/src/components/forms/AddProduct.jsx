import { useForm } from 'react-hook-form';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import Navigation from '../common/Navigation';

const AddProduct = () => {
    const { employeeId } = useParams();
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm();

    const onSubmit = async (formData) => {
        if (formData.category && formData.isAvailable) {
            try {
                formData = {
                    ...formData,
                    price: parseFloat(formData.price),
                    isAvailable: formData.isAvailable === 'true' ? true : false
                };
                const restaurantId = formData.restaurantId;
                delete formData.restaurantId;

                console.log(formData)

                const { data } = await axios.post(`http://localhost:8080/api/restaurants/${restaurantId}/products/add?employeeId=${employeeId}`, [formData], {
                    headers: { 'Content-Type': 'application/json' }
                });
    
                console.log(data);
                toast.success('Added Product!', { autoClose: 2000 })
            } catch (e) {
                console.error('There was an error submitting the form: ', e);
                toast.error(e.response.data.message, { autoClose: 2000 });
            }
        }
        else {
            toast.error('Invalid category or isAvailable', { autoClose: 2000 });
        }
    }

    return (
        <div className='h-screen w-full flex flex-col justify-center items-center bg-ivory relative overflow-scroll'>
            <Navigation />
            <ToastContainer />
            <form className='user-order' onSubmit={handleSubmit(onSubmit)}>

                <input className='form-input' type='text' placeholder='Restaurant ID' {
                    ...register('restaurantId', {
                    required: 'Restaurant ID required', 
                    pattern: /^[1-9]\d*$/
                    })}
                />

                {errors.restaurantId && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.restaurantId.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Name' {
                    ...register('name', {
                    required: 'Product name required', 
                    pattern: {
                        value: /^[\p{L}0-9 '&\-.,]+$/u,
                        message: 'Syntax error'
                    }
                    })}
                />

                {errors.name && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.name.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Price' {
                    ...register('price', {
                    required: 'Product price required', 
                    pattern: /^\+?(?:\d+\.\d*|\d*\.\d+)(?:[eE]\+?\d+)?$/
                    })}
                />

                {errors.price && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.price.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Description' {
                    ...register('description', {
                    required: 'Product description required', 
                    pattern: /^[\p{L}0-9 '&\-.,]+$/u
                    })}
                />

                {errors.description && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.description.message}</div>
                )}

                <select className='form-input' placeholder='Category'
                    {...register('category', {
                        required: 'Product category required'
                    })}
                >
                    <option value=''>Category</option>
                    <option value='SALADS'>SALADS</option>
                    <option value='MAIN'>MAIN</option>
                    <option value='DESSERTS'>DESSERTS</option>
                    <option value='DRINKS'>DRINKS</option>
                    <option value='ALCOHOLS'>ALCOHOLS</option>
                </select>

                <select className='form-input' placeholder='Is Available'
                    {...register('isAvailable', {
                        required: 'Product availability required'
                    })}
                >
                    <option value=''>Is Available</option>
                    <option value='true'>Available</option>
                    <option value='false'>Not Available</option>
                </select>

                <input className='form-input' type='text' placeholder='Cuisine Name' {
                    ...register('cuisineName', {
                    required: 'Product cuisine name required', 
                    pattern: /^[A-Z][a-z]+$/
                    })}
                />

                {errors.cuisineName && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.cuisineName.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Restaurant Name' {
                    ...register('restaurantName', {
                    required: 'Product restaurant name required', 
                    pattern: /^[\p{L}0-9 '&\-.,]+$/u
                    })}
                />

                {errors.restaurantName && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.restaurantName.message}</div>
                )}
                
                <button className='bg-peach-400 hover:bg-peach-100 rounded-xl h-14 w-[30%]' disabled={isSubmitting}>
                    {isSubmitting ? 'Loading...' : 'Submit'}
                </button>
            </form>
        </div>
    );
}

export default AddProduct;