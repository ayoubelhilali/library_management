import { Link } from "react-router-dom";
import { BookOpen, RefreshCw, ShieldCheck } from "lucide-react";
import FeatureItem from "../components/FeatureItem";

function Home() {
    const libraryFeatures = [
        {
            icon: <BookOpen className="w-12 h-12 text-indigo-400" />,
            title: "Catalog",
            description: "Effortless book tracking and organization."
        },
        {
            icon: <RefreshCw className="w-12 h-12 text-indigo-400" />,
            title: "Circulation",
            description: "Streamlined borrow and return workflows."
        },
        {
            icon: <ShieldCheck className="w-12 h-12 text-indigo-400" />,
            title: "Security",
            description: "Role-based access for admins and members."
        }
    ];

    return (
        <div className="min-h-screen bg-slate-900 text-white">
            {/* Hero Section */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center min-h-screen py-16 lg:py-0">
                    <div className="z-10">
                        <span className="uppercase font-bold tracking-widest text-indigo-400 mb-4 block">
                            Next-Gen Archive
                        </span>
                        <h1 className="text-5xl lg:text-7xl font-bold mb-6 leading-tight">
                            The future of <br/>
                            <span className="font-serif italic text-indigo-200">Knowledge.</span>
                        </h1>
                        <p className="text-xl text-slate-300 mb-10 lg:pr-12">
                            A sophisticated, centralized platform designed to manage
                            collections, track circulation, and empower readers.
                        </p>
                        <div className="flex flex-wrap gap-4">
                            <Link to="/login" className="px-8 py-3 bg-indigo-600 text-white font-medium rounded-lg shadow-lg hover:bg-indigo-700 transition no-underline">
                                Get Started
                            </Link>
                            <Link to="/register" className="px-8 py-3 bg-transparent text-white font-medium rounded-lg border border-slate-600 hover:bg-slate-800 hover:border-slate-500 transition no-underline">
                                Create Account 
                            </Link>
                        </div>
                    </div>

                    <div className="hidden lg:block relative">
                        <img
                            src="https://images.unsplash.com/photo-1524995997946-a1c2e315a42f?auto=format&fit=crop&w=800&q=80"
                            alt="Library"
                            className="w-full h-auto rounded-3xl shadow-2xl object-cover"
                        />
                        <div className="absolute -bottom-8 -left-8 bg-white/10 backdrop-blur-md border border-white/20 p-6 rounded-2xl shadow-xl max-w-sm">
                            <p className="mb-2 font-bold text-lg">"A room without books is like a body without a soul."</p>
                            <small className="text-indigo-200">— Cicero</small>
                        </div>
                    </div>
                </div>
            </div>

            {/* Features Section */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pb-20">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    {libraryFeatures.map((feature, index) => (
                        <div key={index}>
                            <FeatureItem {...feature} />
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Home;