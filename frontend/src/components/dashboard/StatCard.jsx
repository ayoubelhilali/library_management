function StatCard({ title, value, icon }) {
  return (
    <div className="bg-slate-900 border border-slate-800 p-6 rounded-2xl shadow-lg">
      <div className="text-3xl mb-4">{icon}</div>
      <h3 className="text-slate-400 text-sm">{title}</h3>
      <p className="text-3xl font-bold mt-2">{value}</p>
    </div>
  );
}

export default StatCard;
