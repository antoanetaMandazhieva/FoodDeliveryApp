import Navigation from '../common/Navigation';
import Hero from './Hero';
import Cuisines from './Cuisines';
import PopRestaurants from './PopRestaurants';
import Jobs from './Jobs';
import Footer from '../common/Footer';

const Home = () => {
    return (
        <div className='bg-ivory h-screen'>
            <Navigation />
            <main className='flex-grow w-full'>
                <Hero />
                <Cuisines />
                <PopRestaurants />
                <Jobs />
            </main>
            <Footer />
        </div>
    )
}

export default Home;