import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import Navigation from '../common/Navigation';

const SupplierPage = () => {
    const { supplierId } = useParams();

    return (
        <div className='flex flex-col justify-between items-center h-screen overflow-y-scroll'>
            <Navigation />
            <div className='flex justify-around items-center m-10 py-5 bg-ivory/5 rounded-4xl 
                bg-clip-padding backdrop-filter backdrop-blur-2xl border border-ivory shadow-zinc-800 shadow-xl
                w-full sm:w-[90%] md:w-[80%] h-screen'>
                    

                <Link to={`/profile/supplier/${supplierId}/orders-dashboard`}>
                    <div className='flex justify-center items-center'>
                        <button className='bg-teal hover:bg-teal-600 text-ivory text-3xl font-quicksand font-bold p-3 rounded-4xl'>
                            Orders Dashboard
                        </button>
                    </div>
                </Link>

                <Link to={`/profile/supplier/${supplierId}/orders-assigned`}>
                    <div className='flex justify-center items-center'>
                        <button className='bg-teal hover:bg-teal-600 text-ivory text-3xl font-quicksand font-bold p-3 rounded-4xl'>
                            Orders Assigned
                        </button>
                    </div>
                </Link>
            </div>
        </div>
    );
}

export default SupplierPage;