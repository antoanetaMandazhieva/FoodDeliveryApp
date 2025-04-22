import axios from 'axios';
import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import Navigation from '../common/Navigation';
import ProfileUpdateForm from '../forms/ProfileUpdateForm';

const ProfilePage = () => {
    const { userId } = useParams();

    const [userDetails, setUserDetails] = useState();
    const [displayForm, setDisplayForm] = useState();
    console.log(userDetails)

    useEffect(() => {
        const handleUserDetails = async () => {
            const { data } = await axios.get(`http://localhost:8080/api/users/${userId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            setUserDetails(data);
        }
        handleUserDetails();
    }, []);

    const addressesUl = userDetails && userDetails.addresses.map(addr => (
        <li className='max-sm:text-base text-lg text-ivory font-quicksand'>
            {`${addr.street}, ${addr.city}, ${addr.postalCode}, ${addr.country}`}
        </li>));

    return (
        <div className='h-screen w-full bg-ivory overflow-y-scroll'>
            <Navigation />
            <main className='flex max-md:flex-col justify-around items-center p-6'>
                {displayForm 
                    ? <ProfileUpdateForm 
                        setUserDetails={setUserDetails}
                        userDetails={userDetails} 
                        setDisplayForm={() => setDisplayForm(prev => !prev)}
                    /> 
                    : <section className='h-[35rem] w-full md:w-[40%] max-md:mb-8 flex flex-col justify-between items-center bg-peach-400 rounded-4xl p-4'>
                        <svg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke-width='1.5' className='size-12 stroke-ivory'>
                        <path stroke-linecap='round' stroke-linejoin='round' d='M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z' />
                        </svg>
                        <h1 className='max-sm:text-2xl text-3xl text-ivory font-quicksand font-bold'>
                            {userDetails && `${userDetails.name} ${userDetails.surname}`}
                        </h1>
                        <h2 className='max-sm:text-xl text-2xl text-ivory font-quicksand font-bold'>
                            {userDetails && userDetails.username}
                        </h2>
                        <h2 className='max-sm:text-xl text-2xl text-ivory font-quicksand'>
                            {userDetails && userDetails.email}
                        </h2>
                        <h3 className='max-sm:text-lg text-xl text-ivory font-quicksand'>
                            {userDetails && userDetails.phoneNumber}
                        </h3>
                        <h3 className='max-sm:text-lg text-xl text-ivory font-quicksand'>
                            {userDetails && `${userDetails.gender.toLowerCase()}, ${userDetails.dateOfBirth}`}
                        </h3>
                        <div className='flex flex-col justify-around items-center'>
                            <h3 className='max-sm:text-lg text-xl text-ivory font-quicksand font-bold'>
                                Addresses:
                            </h3>
                            <ul>
                                {addressesUl && (addressesUl.length > 3 ? addressesUl.splice(0, 3) : addressesUl)}
                            </ul>
                        </div>
                    <button className='p-4 bg-ivory max-sm:text-xl text-2xl text-peach-400 font-quicksand font-bold rounded-3xl hover:scale-95 hover:cursor-pointer' onClick={() => setDisplayForm(prev => !prev)}>
                        {displayForm ? 'Close' : 'Update Details'}
                    </button>
                </section>}
                <section className='w-full md:w-[50%] grid grid-rows-2 grid-cols-2 gap-4'>
                    <Link to={`/profile/${userId}/restaurant-review`}>
                        <button className='h-full w-full p-2 bg-peach-400 max-sm:text-2xl sm:text-3xl lg:text-4xl text-ivory font-quicksand font-bold rounded-3xl hover:scale-95 hover:cursor-pointer'>
                            Leave Restaurant Review
                        </button>
                    </Link>
                    <Link to={`/profile/${userId}/supplier-review`}>
                        <button className='h-full w-full p-2 bg-peach-400 max-sm:text-2xl sm:text-3xl lg:text-4xl text-ivory font-quicksand font-bold rounded-3xl hover:scale-95 hover:cursor-pointer'>
                            Leave Courrier Review
                        </button>
                    </Link>
                    <Link className='grid-cols-subgrid col-span-2' to={`/profile/${userId}/orders`}>
                        <button className='h-full w-full p-2 bg-peach-400 max-sm:text-2xl sm:text-3xl lg:text-4xl text-ivory font-quicksand font-bold rounded-3xl hover:scale-95 hover:cursor-pointer'>
                            Orders History
                        </button>
                    </Link>
                </section>
            </main>
        </div>
    );
}

export default ProfilePage;