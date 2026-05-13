import React from "react";

function ReservationList({ reservations, books }) {
  if (reservations.length === 0) {
    return (
      <div className="bg-slate-900 border border-slate-800 p-8 rounded-2xl text-center">
        <p className="text-slate-400">No reservations found.</p>
      </div>
    );
  }

  return (
    <div className="bg-slate-900 border border-slate-800 rounded-2xl overflow-hidden">
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-slate-800/50">
            <tr>
              <th className="px-6 py-4 border-b border-slate-800 text-left">
                Book
              </th>
              <th className="px-6 py-4 border-b border-slate-800 text-left">
                Date
              </th>
              <th className="px-6 py-4 border-b border-slate-800 text-left">
                Status
              </th>
              <th className="px-6 py-4 border-b border-slate-800 text-left">
                Queue Position
              </th>
            </tr>
          </thead>
          <tbody>
            {reservations.map((reservation, index) => {
              const book = books.find(
                (b) => b.id === (reservation.bookID || reservation.bookId),
              );

              return (
                <tr
                  key={reservation.id || `reservation-${index}`}
                  className="hover:bg-slate-800/20 transition-colors"
                >
                  <td className="px-6 py-4 border-b border-slate-800">
                    <span className="font-medium">
                      {book ? book.title : "Unknown Book"}
                    </span>
                  </td>
                  <td className="px-6 py-4 border-b border-slate-800 text-slate-400">
                    {reservation.reservationDate}
                  </td>
                  <td className="px-6 py-4 border-b border-slate-800">
                    <span
                      className={`inline-block px-3 py-1 rounded-full text-xs font-semibold ${
                        reservation.status === "PENDING"
                          ? "bg-yellow-500/20 text-yellow-400"
                          : reservation.status === "FULFILLED"
                            ? "bg-green-500/20 text-green-400"
                            : "bg-red-500/20 text-red-400"
                      }`}
                    >
                      {reservation.status}
                    </span>
                  </td>
                  <td className="px-6 py-4 border-b border-slate-800 font-medium text-slate-300">
                    {reservation.queuePosition || "-"}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default ReservationList;