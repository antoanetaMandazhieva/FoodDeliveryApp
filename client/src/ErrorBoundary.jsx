import React from 'react';

class ErrorBoundary extends React.Component {
    constructor(props) {
        super(props);
        this.state = { hasError: false, error: null };
    }

    static getDerivedStateFromError(error) {
        return { hasError: true, error };
    }

    componentDidCatch(error, errorInfo) {
        console.error("ErrorBoundary caught an error:", error, errorInfo);
    }

    handleReload = () => {
        window.location.reload();
    };

    render() {
        if (this.state.hasError) {
            return (
                <div className="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-peach-100 to-peach-300 p-6">
                    <h1 className="text-4xl font-bold text-peach-400 mb-4">
                        Oops! Something went wrong.
                    </h1>
                    <p className="text-lg text-black mb-6 text-center">
                        An unexpected error has occurred. Please try refreshing the page.
                    </p>
                    <button 
                        className="px-6 py-3 bg-peach-400 text-black font-bold rounded-full hover:bg-peach-200 transition-all"
                        onClick={this.handleReload}
                    >
                        Reload Page
                    </button>
                </div>
            );
        }

        return this.props.children; 
    }
}

export default ErrorBoundary;