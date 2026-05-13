import { useEffect, useState } from "react";
import API from "../api/api";

export default function NotificationPage({ user, onNotificationsMarkedRead }) {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {

    const loadNotifications = async () => {

      try {

        // GET USER NOTIFICATIONS
        const response = await API.get(
          `/notifications?memberId=${user.id}`
        ).catch(err => {
          console.error("Error fetching notifications:", err.response?.status || err.message);
          return { data: [] };
        });

        // MARK ALL AS READ
        if (response.data && response.data.length > 0) {
          await API.put(
            `/notifications?markAllAsRead=${user.id}`
          ).catch(err => {
            console.error("Error marking notifications as read:", err.response?.status || err.message);
          });
        }

        // UPDATE LOCAL STATE with new data and notify dashboard
        const updatedNotifications = (response.data || []).map((notification) => ({
          ...notification,
          read: true,
        }));
        
        setNotifications(updatedNotifications);

        // REFRESH SIDEBAR BADGE immediately
        if (onNotificationsMarkedRead) {
          onNotificationsMarkedRead();
        }

      } catch (error) {

        console.error(
          "Error in loadNotifications:",
          error.message
        );
        setNotifications([]);

      } finally {

        setLoading(false);
      }
    };

    if (user?.id) {

      loadNotifications();
    }

  }, [user]);

  const deleteNotification = async (id) => {

    try {

      await API.delete(
        `/notifications?id=${id}`
      );

      setNotifications((prev) =>
        prev.filter(
          (notification) =>
            notification.notificationId !== id
        )
      );

    } catch (error) {

      console.error(error);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen text-slate-300">
        Loading notifications...
      </div>
    );
  }

  return (
    <>
      {/* HEADER */}
      <div className="flex items-center justify-between mb-8">

        <div>
          <h2 className="text-3xl font-bold mb-2">
            Notifications
          </h2>

          <p className="text-slate-400">
            Stay updated with your library activity
          </p>
        </div>

        <div className="bg-slate-900 border border-slate-800 px-5 py-3 rounded-2xl">
          <span className="text-slate-300 font-medium">
            {notifications.filter(n => !n.read).length} Unread
          </span>
        </div>
      </div>

      {/* EMPTY STATE */}
      {notifications.length === 0 ? (

        <div className="bg-slate-900 border border-slate-800 rounded-2xl p-12 text-center">

          <h3 className="text-2xl font-bold mb-3">
            No notifications
          </h3>

          <p className="text-slate-400">
            You don't have any notifications yet.
          </p>

        </div>

      ) : (

        <div className="space-y-5">

          {notifications.map((notification, index) => (

            <div
              key={notification.notificationId || notification.id || `notification-${index}`}
              className={`rounded-2xl border p-6 transition-all ${
                notification.read
                  ? "bg-slate-900 border-slate-800"
                  : "bg-indigo-950/40 border-indigo-700"
              }`}
            >

              <div className="flex items-start justify-between gap-6">

                {/* LEFT */}
                <div className="flex-1">

                  <div className="flex items-center gap-3 mb-3">

                    {!notification.read && (
                      <div className="w-3 h-3 rounded-full bg-indigo-400 animate-pulse"></div>
                    )}

                    <h3 className="text-xl font-semibold">
                      {notification.read
                        ? "Notification"
                        : "New Notification"}
                    </h3>
                  </div>

                  <p className="text-slate-300 leading-relaxed">
                    {notification.message}
                  </p>

                  <div className="mt-5 flex gap-3">

                    <button
                      onClick={() =>
                        deleteNotification(
                          notification.notificationId
                        )
                      }
                      className="px-4 py-2 rounded-xl border border-red-500 text-red-400 hover:bg-red-500 hover:text-white transition text-sm font-medium"
                    >
                      Delete
                    </button>

                  </div>
                </div>

                {/* RIGHT */}
                <div className="text-right">

                  <p className="text-slate-400 text-sm mb-3">
                    {notification.sendDate}
                  </p>

                  <span
                    className={`inline-block px-4 py-2 rounded-full text-sm font-medium ${
                      notification.read
                        ? "bg-slate-800 text-slate-300"
                        : "bg-indigo-500/20 text-indigo-300"
                    }`}
                  >
                    {notification.read
                      ? "Read"
                      : "Unread"}
                  </span>

                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </>
  );
}