import Notifications from "../components/dashboard/Notifications";

function NotificationsPage({ notifications = [] }) {
  return (
    <>
      <h2 className="text-2xl font-bold mb-5">Notifications</h2>
      <Notifications notifications={notifications} />
    </>
  );
}

export default NotificationsPage;
