import { useForm } from 'react-hook-form';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { toast, ToastContainer } from 'react-toastify';
import Navigation from '../common/Navigation';

const ChangeUserRole = () => {
    const { adminId } = useParams();

    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm();

    const onSubmit = async (formdata) => {
        try {
            const { data } = await axios.put(`http://localhost:8080/api/users/${adminId}/change-role/${formdata.userId}?role=${formdata.role}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            toast.success('Succesfully updated user role', { autoClose: 2000 });
        }
        catch (e) {
            console.error(e);
            toast.error(e.response.data.message, { autoClose: 2000 });
        }

    }

    return (
        <div className='h-screen w-full flex flex-col justify-center items-center bg-ivory relative overflow-scroll'>
            <Navigation />
            <ToastContainer />
            <form className='user-order' onSubmit={handleSubmit(onSubmit)}>

                <input className='form-input' type='text' placeholder='User ID' {
                    ...register('userId', {
                    required: 'User ID required', 
                    pattern: /^[1-9]\d*$/
                    })}
                />

                {errors.userId && (
                    <div className='text-red-600 font-semibold px-2 text-center'>{errors.userId.message}</div>
                )}

                <select className='form-input' placeholder='Role'
                    {...register('role', {
                        required: 'New role required'
                    })}
                >
                    <option value=''>Role</option>
                    <option value='SUPPLIER'>Supplier</option>
                    <option value='EMPLOYEE'>Employee</option>
                    <option value='ADMIN'>Admin</option>
                </select>

                <button className='bg-peach-400 hover:bg-peach-100 rounded-xl h-14 w-[30%]' disabled={isSubmitting}>
                    {isSubmitting ? 'Loading...' : 'Submit'}
                </button>
            </form>
        </div>
    );
}

export default ChangeUserRole;