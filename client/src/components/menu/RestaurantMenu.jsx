import Navigation from '../common/Navigation';
import MenuSection from './MenuSection';
import SearchBarMenu from './SearchBarMenu';

const RestaurantMenu = () => {
    return (
        <div className='h-screen bg-ivory'>
            <Navigation />
            <main className='w-full flex-grow bg-ivory'>
                <div className='w-full p-6'>
                    <SearchBarMenu />
                </div>
                <div className='w-full p-6'>
                    <MenuSection />
                </div>
            </main>
        </div>
    );
}

export default RestaurantMenu;