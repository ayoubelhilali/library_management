import { useEffect, useState } from "react";
import axios from "axios";

import API from "../api/api";

export default function ProfilePage() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      // adapte endpoint selon ton backend
      const res = await API.get("/profile");
      setUser(res.data);
    } catch (err) {
      console.error("Error loading profile", err);

      // fallback demo (remove later)
      setUser({
        username: "john_doe",
        email: "john@example.com",
        phone: "123456789",
        role: "MEMBER",
        memberType: "STUDENT",
      });
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-100">
        <p className="text-lg text-slate-600">Loading profile...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 to-slate-200 p-8">
      <div className="max-w-4xl mx-auto">

        {/* Header */}
        <div className="bg-white rounded-3xl shadow-lg p-8 flex items-center justify-between">
          <div className="flex items-center gap-6">

            {/* Avatar */}
            <div className="w-20 h-20 rounded-full bg-slate-900 text-white flex items-center justify-center text-2xl font-bold">
              {user.username.charAt(0).toUpperCase()}
            </div>

            <div>
              <h1 className="text-3xl font-bold text-slate-800">
                {user.username}
              </h1>
              <p className="text-slate-500">{user.email}</p>
            </div>
          </div>

          <span className="px-4 py-2 rounded-full bg-emerald-100 text-emerald-700 font-semibold">
            {user.role}
          </span>
        </div>

        {/* Info Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">

          <div className="bg-white p-6 rounded-2xl shadow-md">
            <h2 className="text-lg font-semibold mb-4">Personal Info</h2>

            <div className="space-y-3 text-slate-600">
              <p><span className="font-medium">Username:</span> {user.username}</p>
              <p><span className="font-medium">Email:</span> {user.email}</p>
              <p><span className="font-medium">Phone:</span> {user.phone}</p>
            </div>
          </div>

          <div className="bg-white p-6 rounded-2xl shadow-md">
            <h2 className="text-lg font-semibold mb-4">Library Info</h2>

            <div className="space-y-3 text-slate-600">
              <p><span className="font-medium">Role:</span> {user.role}</p>
              <p><span className="font-medium">Member Type:</span> {user.memberType}</p>
            </div>
          </div>
        </div>

        {/* Actions */}
        <div className="mt-8 flex gap-4">
          <button className="px-6 py-3 rounded-xl bg-slate-900 text-white hover:scale-105 transition">
            Edit Profile
          </button>

          <button className="px-6 py-3 rounded-xl border border-red-400 text-red-500 hover:bg-red-500 hover:text-white transition">
            Delete Account
          </button>
        </div>

      </div>
    </div>
  );
}
