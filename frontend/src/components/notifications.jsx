import { useEffect, useState } from "react";
import axios from "axios";

import API from "../api/api";

export default function NotificationPage() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      const response = await API.get("/notifications");
      setNotifications(response.data);
    } catch (error) {
      console.error("Error fetching notifications:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-100 p-8">
      <div className="max-w-4xl mx-auto">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-4xl font-bold text-slate-800">
              Notifications
            </h1>
            <p className="text-slate-500 mt-2">
              Stay updated with your library activity.
            </p>
          </div>

          <button className="px-5 py-3 rounded-2xl bg-slate-900 text-white font-medium shadow-lg hover:scale-105 transition-all duration-200">
            Mark all as read
          </button>
        </div>

        <div className="space-y-5">
          {notifications.map((notification) => (
            <div
              key={notification.notificationId}
              className={`rounded-3xl p-6 shadow-md border transition-all duration-300 hover:shadow-xl hover:-translate-y-1 ${
                notification.read
                  ? "bg-white border-slate-200"
                  : "bg-slate-900 text-white border-slate-900"
              }`}
            >
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1">
                  <div className="flex items-center gap-3 mb-3">
                    {!notification.read && (
                      <span className="w-3 h-3 rounded-full bg-emerald-400 animate-pulse"></span>
                    )}

                    <h2 className="text-xl font-semibold">
                      {notification.read ? "Notification" : "New Notification"}
                    </h2>
                  </div>

                  <p
                    className={`text-base leading-relaxed ${
                      notification.read
                        ? "text-slate-600"
                        : "text-slate-200"
                    }`}
                  >
                    {notification.message}
                  </p>
                </div>

                <div className="text-right min-w-[120px]">
                  <p
                    className={`text-sm ${
                      notification.read
                        ? "text-slate-400"
                        : "text-slate-300"
                    }`}
                  >
                    {notification.sendDate}
                  </p>

                  <span
                    className={`inline-block mt-3 px-4 py-2 rounded-full text-sm font-medium ${
                      notification.read
                        ? "bg-slate-200 text-slate-700"
                        : "bg-emerald-400 text-slate-900"
                    }`}
                  >
                    {notification.read ? "Read" : "Unread"}
                  </span>
                </div>
              </div>

              <div className="flex gap-3 mt-6">
                <button className="px-4 py-2 rounded-xl bg-white/10 hover:bg-white/20 transition text-sm font-medium">
                  Open
                </button>

                {!notification.read && (
                  <button className="px-4 py-2 rounded-xl bg-emerald-400 text-slate-900 hover:opacity-90 transition text-sm font-semibold">
                    Mark as read
                  </button>
                )}

                <button className="px-4 py-2 rounded-xl border border-red-300 text-red-400 hover:bg-red-500 hover:text-white transition text-sm font-medium">
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
