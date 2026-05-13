import React from "react";

function FeatureItem({ icon, title, description }) {
    return (
        <div className="bg-slate-800/50 backdrop-blur-sm p-8 rounded-2xl shadow-lg border border-slate-700 h-full hover:bg-slate-800 hover:-translate-y-1 transition duration-300">
            <div className="mb-6 flex justify-start">
                {icon}
            </div>
            <h5 className="font-bold text-xl text-white mb-3">{title}</h5>
            <p className="text-slate-400 text-sm">{description}</p>
        </div>
    );
}

export default FeatureItem;