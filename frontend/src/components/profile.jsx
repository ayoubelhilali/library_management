import { useEffect, useState } from "react";
import API from "../api/api";

export default function ProfilePage({ user }) {
  const [borrowCount, setBorrowCount] = useState(0);
  const [reservationCount, setReservationCount] = useState(0);
  const [returnedCount, setReturnedCount] = useState(0);

  useEffect(() => {
    if (user?.id) {
      fetchStats();
    }
  }, [user]);

  const fetchStats = async () => {
    try {
      // Get borrowings
      const borrowsRes = await API.get("/borrows");
      const userBorrows = borrowsRes.data.filter(
        (b) => b.memberID === user.id || b.memberId === user.id
      );
      
      // Count current borrows (no return date)
      const activeBorrows = userBorrows.filter(b => !b.actualReturnDate).length;
      setBorrowCount(activeBorrows);

      // Count returned books
      const returned = userBorrows.filter(b => b.actualReturnDate).length;
      setReturnedCount(returned);

      // Get reservations
      const reservationsRes = await API.get(`/reservations?memberId=${user.id}`);
      setReservationCount(reservationsRes.data.length);
    } catch (error) {
      console.error("Error fetching stats:", error);
    }
  };

  
  return (
    <>
      <h2 className="text-2xl font-bold mb-6">My Profile</h2>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

        {/* LEFT CARD */}
        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-8 flex flex-col items-center">
          
          {/* Avatar */}
          <div className="w-24 h-24 rounded-full bg-indigo-600 flex items-center justify-center text-4xl font-bold mb-5">
            {user.username?.charAt(0).toUpperCase()}
          </div>

          <h3 className="text-2xl font-bold">
            {user.username}
          </h3>

          <p className="text-slate-400 mt-1">
            {user.email}
          </p>

          <div className="mt-5 flex gap-3 flex-wrap justify-center">
            <span className="px-4 py-1 rounded-full bg-indigo-500/20 text-indigo-400 text-sm">
              {user.role}
            </span>

            <span className="px-4 py-1 rounded-full bg-emerald-500/20 text-emerald-400 text-sm">
              {user.memberType}
            </span>
          </div>

          {/* Buttons */}
          <div className="w-full mt-8 space-y-3">
            <button className="w-full bg-indigo-600 hover:bg-indigo-700 transition py-3 rounded-xl font-medium">
              Edit Profile
            </button>

            <button className="w-full border border-red-500 text-red-400 hover:bg-red-500 hover:text-white transition py-3 rounded-xl font-medium">
              Delete Account
            </button>
          </div>
        </div>

        {/* RIGHT SECTION */}
        <div className="lg:col-span-2 space-y-6">

          {/* Personal Information */}
          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6">
            <h3 className="text-xl font-bold mb-5">
              Personal Information
            </h3>

            <div className="grid md:grid-cols-2 gap-5">

              <div className="bg-slate-800/60 p-4 rounded-xl">
                <p className="text-slate-400 text-sm mb-1">
                  Username
                </p>

                <p className="font-semibold text-lg">
                  {user.username}
                </p>
              </div>

              <div className="bg-slate-800/60 p-4 rounded-xl">
                <p className="text-slate-400 text-sm mb-1">
                  Email
                </p>

                <p className="font-semibold text-lg break-all">
                  {user.email}
                </p>
              </div>

              <div className="bg-slate-800/60 p-4 rounded-xl">
                <p className="text-slate-400 text-sm mb-1">
                  Phone
                </p>

                <p className="font-semibold text-lg">
                  {user.phone || "Not provided"}
                </p>
              </div>

              <div className="bg-slate-800/60 p-4 rounded-xl">
                <p className="text-slate-400 text-sm mb-1">
                  Member Type
                </p>

                <p className="font-semibold text-lg">
                  {user.memberType}
                </p>
              </div>

            </div>
          </div>

          {/* Library Stats */}
          <div className="bg-slate-900 border border-slate-800 rounded-2xl p-6">
            <h3 className="text-xl font-bold mb-5">
              Library Activity
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-5">

              <div className="bg-slate-800/60 rounded-xl p-5 text-center">
                <p className="text-3xl font-bold text-indigo-400">
                  {borrowCount}
                </p>

                <p className="text-slate-400 mt-2">
                  Borrowed Books
                </p>
              </div>

              <div className="bg-slate-800/60 rounded-xl p-5 text-center">
                <p className="text-3xl font-bold text-yellow-400">
                  {reservationCount}
                </p>

                <p className="text-slate-400 mt-2">
                  Reservations
                </p>
              </div>

              <div className="bg-slate-800/60 rounded-xl p-5 text-center">
                <p className="text-3xl font-bold text-emerald-400">
                  {returnedCount}
                </p>

                <p className="text-slate-400 mt-2">
                  Returned Books
                </p>
              </div>

            </div>
          </div>

        </div>
      </div>
    </>
  );
}