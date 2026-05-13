import React from "react";
import {
  Library,
  BookCheck,
  ArrowLeftRight,
  Users,
  Clock,
  Sparkles,
  ChevronRight,
} from "lucide-react";
import StatCard from "../components/dashboard/StatCard";
import BorrowList from "../components/dashboard/BorrowList";
import ReservationList from "../components/dashboard/ReservationList";

function OverviewPage({ books, borrows, reservations, members, user }) {
  const isAdmin = user?.role === "ADMIN";
  const activeBorrows = borrows.filter((b) => !b.actualReturnDate);
  const availableBooks = books.filter((book) => book.status === "AVAILABLE");

  return (
    <div className="max-w-7xl mx-auto">
      {/* Welcome Header */}
      <div className="mb-10">
        <div className="flex items-center gap-2 text-indigo-400 font-semibold mb-2">
          <Sparkles size={18} />
          <span className="uppercase tracking-wider text-xs">
            Dashboard Overview
          </span>
        </div>
        <h1 className="text-4xl font-black text-white">
          Welcome back,{" "}
          <span className="text-indigo-500">{user?.username || "User"}</span>!
        </h1>
        <p className="text-slate-400 mt-2">
          {isAdmin
            ? "Here is what's happening with the library system today."
            : "Manage your readings and upcoming reservations."}
        </p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
        <StatCard
          title="Total Books"
          value={books.length}
          icon={Library}
          color="indigo"
        />
        <StatCard
          title="Available Now"
          value={availableBooks.length}
          icon={BookCheck}
          color="emerald"
        />
        <StatCard
          title={isAdmin ? "Total Borrows" : "Active Borrows"}
          value={isAdmin ? borrows.length : activeBorrows.length}
          icon={ArrowLeftRight}
          color="blue"
        />
        <StatCard
          title={isAdmin ? "Total Members" : "Reservations"}
          value={isAdmin ? members?.length || 0 : reservations?.length || 0}
          icon={isAdmin ? Users : Clock}
          color="purple"
        />
      </div>

      {/* Activity Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
        {/* Recent Borrows */}
        <div className="bg-slate-900/50 border border-slate-800 rounded-3xl p-1">
          <div className="flex items-center justify-between p-6">
            <h2 className="text-xl font-bold text-white flex items-center gap-3">
              <ArrowLeftRight size={20} className="text-indigo-400" />
              {isAdmin ? "Global Recent Borrows" : "My Recent Borrows"}
            </h2>
            <button className="text-slate-500 hover:text-white transition-colors">
              <ChevronRight size={20} />
            </button>
          </div>
          <div className="px-2 pb-2">
            <BorrowList borrows={borrows.slice(0, 5)} books={books} />
          </div>
        </div>

        {/* Recent Reservations */}
        <div className="bg-slate-900/50 border border-slate-800 rounded-3xl p-1">
          <div className="flex items-center justify-between p-6">
            <h2 className="text-xl font-bold text-white flex items-center gap-3">
              <Clock size={20} className="text-purple-400" />
              {isAdmin ? "Newest Reservations" : "My Reservations"}
            </h2>
            <button className="text-slate-500 hover:text-white transition-colors">
              <ChevronRight size={20} />
            </button>
          </div>
          <div className="px-2 pb-2">
            <ReservationList
              reservations={reservations.slice(0, 5)}
              books={books}
            />
          </div>
        </div>
      </div>
    </div>
  );
}

export default OverviewPage;
