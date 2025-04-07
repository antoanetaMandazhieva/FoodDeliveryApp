const Footer = () => {
    return (
        <footer className="bg-zinc-900 h-24 text-gray-300 py-4">
            <div className="container mx-auto flex flex-col md:flex-row justify-between items-center">
                <p className="text-sm">
                    &copy; {new Date().getFullYear()} VarTeam. All rights reserved.
                </p>
                <a 
                    href="https://github.com/antoanetaMandazhieva/FoodDeliveryApp" 
                    target="_blank" 
                    rel="noopener noreferrer" 
                    className="text-sm hover:text-white"
                >
                    GitHub Repository
                </a>
            </div>
        </footer>
    );
};

export default Footer;