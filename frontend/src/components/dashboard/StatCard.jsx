import React from "react";

function StatCard({ title, value, icon: Icon, color }) {
  const colors = {
    indigo: "bg-indigo-500/10 text-indigo-500 border-indigo-500/20",
    emerald: "bg-emerald-500/10 text-emerald-500 border-emerald-500/20",
    blue: "bg-blue-500/10 text-blue-500 border-blue-500/20",
    purple: "bg-purple-500/10 text-purple-500 border-purple-500/20",
  };

  return (
    <div className="bg-slate-900 border border-slate-800 p-6 rounded-3xl hover:border-slate-700 transition-all group">
      <div className="flex items-center justify-between mb-4">
        <div
          className={`p-3 rounded-2xl border ${colors[color] || colors.indigo}`}
        >
          <Icon size={24} />
        </div>
      </div>
      <div>
        <p className="text-slate-500 text-sm font-bold uppercase tracking-widest">
          {title}
        </p>
        <h3 className="text-3xl font-black text-white mt-1 group-hover:scale-105 transition-transform origin-left">
          {value}
        </h3>
      </div>
    </div>
  );
}

export default StatCard;
