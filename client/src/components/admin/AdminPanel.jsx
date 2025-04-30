import { useParams, Link } from 'react-router-dom';
import Navigation from '../common/Navigation';
import { useEffect, useState } from 'react';
import { format } from 'date-fns'
import axios from 'axios';


const AdminPanel = () => {
    const { adminId } = useParams();

    const [timeNow, setTimeNow] = useState(new Date());
    const [dailyRevenue, setDailyRevenue] = useState('');
    const [weeklyRevenue, setWeeklyRevenue] = useState('');
    const [monthlyRevenue, setMonthlyRevenue] = useState('');
    
    const getDailyRevenue = async () => {
        try {
            const fromTime = format(new Date(timeNow - 24 * 60 * 60 * 1000), "yyyy-MM-dd'T'HH:mm:ss");
            const toTime = format(timeNow, "yyyy-MM-dd'T'HH:mm:ss");

            const { data } = await axios.get(`http://localhost:8080/api/orders/revenue?from=${fromTime}&to=${toTime}&adminId=${adminId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setDailyRevenue(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    const getWeeklyRevenue = async () => {
        try {
            const fromTime = format(new Date(timeNow - 7 * 24 * 60 * 60 * 1000), "yyyy-MM-dd'T'HH:mm:ss");
            const toTime = format(timeNow, "yyyy-MM-dd'T'HH:mm:ss");

            const { data } = await axios.get(`http://localhost:8080/api/orders/revenue?from=${fromTime}&to=${toTime}&adminId=${adminId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setWeeklyRevenue(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    const getMonthlyRevenue = async () => {
        try {
            const fromTime = format(new Date(timeNow.getMonth() - 1), "yyyy-MM-dd'T'HH:mm:ss");
            const toTime = format(timeNow, "yyyy-MM-dd'T'HH:mm:ss");

            const { data } = await axios.get(`http://localhost:8080/api/orders/revenue?from=${fromTime}&to=${toTime}&adminId=${adminId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setMonthlyRevenue(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    useEffect(() => {
        getDailyRevenue();
        getWeeklyRevenue();
        getMonthlyRevenue();
    }, []);
    
    return (
        <div className='flex flex-col justify-between items-center h-screen overflow-y-scroll'>
            <Navigation />
            <div className='grid grid-rows-4 grid-cols-3 gap-6 m-10 py-5 bg-ivory/5 rounded-4xl 
                bg-clip-padding backdrop-filter backdrop-blur-2xl border border-ivory shadow-zinc-800 shadow-xl
                w-full sm:w-[90%] md:w-[80%] h-screen'>
                    
                <div className='flex justify-around items-center grid-cols-subgrid col-span-3 grid-rows-subgrid row-span-2'>
                    <div className='flex flex-col justify-between items-center bg-ivory rounded-full shadow-2xl shadow-black p-4 w-[30%]'>
                        <h1 className='text-2xl text-emerald-500 font-playfair font-bold'>
                            +{parseFloat(dailyRevenue).toFixed(2)}$
                        </h1>
                        <h2 className='text-xl font-quicksand'>
                            Daily Revenue
                        </h2>
                    </div>

                    <div className='flex flex-col justify-between items-center bg-ivory rounded-full shadow-2xl shadow-black p-4 w-[30%]'>
                        <h1 className='text-2xl text-emerald-500 font-playfair font-bold'>
                            +{parseFloat(weeklyRevenue).toFixed(2)}$
                        </h1>
                        <h2 className='text-xl font-quicksand'>
                            Weekly Revenue
                        </h2>
                    </div>

                    <div className='flex flex-col justify-between items-center bg-ivory rounded-full shadow-2xl shadow-black p-4 w-[30%]'>
                        <h1 className='text-2xl text-emerald-500 font-playfair font-bold'>
                            +{parseFloat(monthlyRevenue).toFixed(2)}$
                        </h1>
                        <h2 className='text-xl font-quicksand'>
                            Monthly Revenue
                        </h2>
                    </div>
                </div>

                <div className='flex justify-center items-center grid-cols-subgrid col-span-3'>
                    <Link to={`/profile/admin/${adminId}/change-role`}>
                        <button className='bg-teal hover:bg-teal-600 text-ivory text-3xl font-quicksand font-bold p-3 rounded-4xl'>
                            Change User Role
                        </button>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default AdminPanel;