import React from "react";
import {
  LayoutDashboard,
  BookOpen,
  Library,
  BookmarkCheck,
  Bell,
  User,
  Users,
  RefreshCw,
  LogOut,
} from "lucide-react";

function Sidebar({ user, activeTab, setActiveTab, logout, unreadCount = 0}) {
  const memberItems = [
    { key: "overview", label: "Overview", icon: LayoutDashboard },
    { key: "browse", label: "Browse Books", icon: BookOpen },
    { key: "borrows", label: "My Borrows", icon: Library },
    { key: "reservations", label: "My Reservations", icon: BookmarkCheck },
    { key: "notifications", label: "Notifications", icon: Bell, badge: unreadCount },
    { key: "profile", label: "Profile", icon: User },
  ];

  const adminItems = [
    { key: "overview", label: "Overview", icon: LayoutDashboard },
    { key: "adminBooks", label: "Manage Books", icon: BookOpen },
    { key: "adminMembers", label: "Manage Members", icon: Users },
    { key: "borrows", label: "All Borrows", icon: RefreshCw },
    { key: "reservations", label: "All Reservations", icon: BookmarkCheck },
    { key: "profile", label: "Profile", icon: User },
  ];

  const items = user.role === "ADMIN" ? adminItems : memberItems;

  return (
    <aside className="fixed left-0 top-0 h-screen w-72 bg-slate-900 border-r border-slate-800 p-6 hidden md:flex flex-col">
      <div className="flex items-center gap-3 mb-10 px-2">
        <div className="h-8 w-8 bg-indigo-600 rounded-lg flex items-center justify-center">
          <Library size={20} className="text-white" />
        </div>
        <h1 className="text-xl font-bold text-white tracking-tight">
          Library Panel
        </h1>
      </div>

      <nav className="space-y-1 flex-1">
        {items.map((item) => {
          const Icon = item.icon;
          const isActive = activeTab === item.key;

          return (
            <button
              key={item.key}
              onClick={() => setActiveTab(item.key)}
              className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl cursor-pointer transition-all duration-200 group ${
                isActive
                  ? "bg-indigo-600 text-white shadow-lg shadow-indigo-900/20"
                  : "text-slate-400 hover:bg-slate-800 hover:text-slate-100"
              }`}
            >
              <Icon
                size={20}
                className={`${isActive ? "text-white" : "text-slate-500 group-hover:text-indigo-400"}`}
              />
              <span className="font-medium flex-1 text-left">{item.label}</span>
              {item.badge > 0 && (
                <span className="bg-red-500 text-white text-xs font-bold px-2 py-0.5 rounded-full">
                  {item.badge}
                </span>
              )}
            </button>
          );
        })}
      </nav>

      <button
        onClick={logout}
        className="mt-auto flex items-center justify-center cursor-pointer gap-2 w-full bg-slate-800 hover:bg-red-900/30 text-slate-300 hover:text-red-400 px-4 py-3 rounded-xl border border-slate-700 hover:border-red-800/50 transition-all duration-200 font-medium"
      >
        <LogOut size={18} />
        Logout
      </button>
    </aside>
  );
}

export default Sidebar;
