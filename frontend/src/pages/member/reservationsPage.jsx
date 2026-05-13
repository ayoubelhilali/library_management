import ReservationList from "../../components/dashboard/ReservationList";

function ReservationsPage({ books, reservations, refreshNotifications }) {
  return (
    <>
      <h2 className="text-2xl font-bold mb-5">My Reservations</h2>
      <ReservationList reservations={reservations} books={books} refreshNotifications={refreshNotifications} />
    </>
  );
}

export default ReservationsPage;
