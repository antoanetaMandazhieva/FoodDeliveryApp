import { useForm } from 'react-hook-form';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import Navigation from '../common/Navigation';

const RemoveProduct = () => {
    const { employeeId } = useParams();
    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm();

    const onSubmit = async (formData) => {
        try {
            const { data } = await axios.put(`http://localhost:8080/api/restaurants/${formData.restaurantId}/remove-product/${formData.productId}/by/${employeeId}`, [formData], {
                headers: { 'Content-Type': 'application/json' }
            });
    
            console.log(data);
            toast.success('Deleted Product!', { autoClose: 2000 })
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

                <input className='form-input' type='text' placeholder='Restaurant ID' {
                    ...register('restaurantId', {
                    required: 'Restaurant ID required', 
                    pattern: /^[1-9]\d*$/
                    })}
                />

                {errors.restaurantId && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.restaurantId.message}</div>
                )}

                <input className='form-input' type='text' placeholder='Product ID' {
                    ...register('productId', {
                    required: 'Product ID required', 
                    pattern: /^[1-9]\d*$/
                    })}
                />

                {errors.productId && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.productId.message}</div>
                )}

                
                <button className='bg-peach-400 hover:bg-peach-100 rounded-xl h-14 w-[30%]' disabled={isSubmitting}>
                    {isSubmitting ? 'Loading...' : 'Submit'}
                </button>
            </form>
        </div>
    );
}

export default RemoveProduct;