function Sidebar({ user, activeTab, setActiveTab, logout }) {
  const memberItems = [
    { key: "overview", label: "📊 Overview" },
    { key: "browse", label: "📖 Browse Books" },
    { key: "borrows", label: "📚 My Borrowed Books" },
    { key: "reservations", label: "📌 My Reservations" },
    { key: "notifications", label: "🔔 Notifications" },
    { key: "profile", label: "👤 Profile" },
  ];

  const adminItems = [
    { key: "overview", label: "📊 Overview" },
    { key: "adminBooks", label: "📚 Manage Books" },
    { key: "adminMembers", label: "👥 Manage Members" },
    { key: "borrows", label: "🔄 All Borrows" },
    { key: "reservations", label: "📌 All Reservations" },
    { key: "profile", label: "👤 Profile" },
  ];

  const items = user.role === "ADMIN" ? adminItems : memberItems;

  return (
    <aside className="fixed left-0 top-0 h-screen w-72 bg-slate-900 border-r border-slate-800 p-6 hidden md:flex flex-col">
      <h1 className="text-2xl font-bold text-indigo-400 mb-10">
        Library Panel
      </h1>

      <nav className="space-y-3 flex-1">
        {items.map((item) => (
          <button
            key={item.key}
            onClick={() => setActiveTab(item.key)}
            className={`w-full text-left px-4 py-3 rounded-xl transition ${
              activeTab === item.key
                ? "bg-indigo-600 text-white"
                : "text-slate-300 hover:bg-slate-800"
            }`}
          >
            {item.label}
          </button>
        ))}
      </nav>

      <button
        onClick={logout}
        className="bg-red-600 hover:bg-red-700 px-4 py-3 rounded-xl"
      >
        Logout
      </button>
    </aside>
  );
}

export default Sidebar;
