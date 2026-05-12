import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

function Dashboard() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);

  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (!storedUser) {
      navigate("/login");
      return;
    }

    setUser(JSON.parse(storedUser));
  }, []);

  if (!user) {
    return <h1 className="text-white">Loading...</h1>;
  }

  return (
    <div className="min-h-screen bg-slate-900 text-white p-8">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-4xl font-bold">Welcome, {user.username}</h1>

          <p className="text-slate-400 mt-2">Role: {user.role}</p>
        </div>

        <button
          onClick={() => {
            localStorage.removeItem("user");
            navigate("/login");
          }}
          className="bg-red-600 hover:bg-red-700 px-5 py-2 rounded-lg"
        >
          Logout
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-slate-800 p-6 rounded-2xl shadow-lg">
          <h2 className="text-2xl font-semibold mb-2">📚 Books</h2>

          <p className="text-slate-400">Manage and explore library books.</p>
        </div>

        <div className="bg-slate-800 p-6 rounded-2xl shadow-lg">
          <h2 className="text-2xl font-semibold mb-2">🔄 Borrows</h2>

          <p className="text-slate-400">Track borrowed and returned books.</p>
        </div>

        <div className="bg-slate-800 p-6 rounded-2xl shadow-lg">
          <h2 className="text-2xl font-semibold mb-2">👤 Profile</h2>

          <p className="text-slate-400">View your account information.</p>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
