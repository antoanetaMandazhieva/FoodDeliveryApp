import { Link, useParams } from 'react-router-dom';
import OrderHistoryItem from '../common/OrderHistoryItem';
import { useEffect, useState } from 'react';
import axios from 'axios';
import Navigation from '../common/Navigation';

const EmployeePage = () => {
    const { employeeId } = useParams();

    return (
        <div className='flex flex-col justify-between items-center h-screen overflow-y-scroll'>
            <Navigation />
            <div className='grid grid-rows-3 grid-cols-2 gap-6 m-10 py-5 bg-ivory/5 rounded-4xl 
                bg-clip-padding backdrop-filter backdrop-blur-2xl border border-ivory shadow-zinc-800 shadow-xl
                w-full sm:w-[90%] md:w-[80%] h-screen'>
                    

                <Link to={`/profile/employee/${employeeId}/add-product`}>
                    <div className='flex justify-center items-center'>
                        <button className='bg-teal hover:bg-teal-600 text-ivory text-3xl font-quicksand font-bold p-3 rounded-4xl'>
                            Add Product
                        </button>
                    </div>
                </Link>

                <Link to={`/profile/employee/${employeeId}/remove-product`}>
                    <div className='flex justify-center items-center'>
                        <button className='bg-teal hover:bg-teal-600 text-ivory text-3xl font-quicksand font-bold p-3 rounded-4xl'>
                            Remove Product
                        </button>
                    </div>
                </Link>

                <Link 
                    to={`/profile/employee/${employeeId}/create-restaurant`}
                    className='grid-cols-subgrid col-span-2'
                >
                    <div className='flex justify-center items-center'>
                        <button className='bg-teal hover:bg-teal-600 text-ivory text-3xl font-quicksand font-bold p-3 rounded-4xl'>
                            Create Restaurant
                        </button>
                    </div>
                </Link>

                <Link 
                    to={`/profile/employee/${employeeId}/orders-dashboard`}
                    className='grid-cols-subgrid col-span-2'
                >
                    <div className='flex justify-center items-center'>
                        <button className='bg-teal hover:bg-teal-600 text-ivory text-3xl font-quicksand font-bold p-3 rounded-4xl'>
                            Orders Dashboard
                        </button>
                    </div>
                </Link>
            </div>
        </div>
    );
}

export default EmployeePage;