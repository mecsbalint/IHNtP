import { Link } from "react-router-dom";
import avatarPlaceholder from "../../assets/avatar_placeholder.png";
import { useAuthContext } from "../../hooks/useAuthContext";
import { useLogout } from "../../hooks/useLogout";

function CustomNavBar() {
  const {user, isLoggedIn} = useAuthContext();
  const logout = useLogout();

  async function handleLogoutClick() {
      await logout();
  }

  return (
    <div className="navbar bg-blue-400 shadow-sm">
      <div className="navbar-start gap-2">
        <Link to={`/`} className="text-2xl font-semibold text-amber-50 p-2">
          IHNtP
        </Link>
        <input
          type="text"
          disabled={true}
          placeholder="Under construction"
          className="input input-bordered w-24 md:w-auto"
        />
      </div>
      <div className={`navbar-center ${isLoggedIn ? "" : "hidden"}`}>
        <Link to={`/games/add`} className="text-xl font-semibold text-amber-50 p-2 pb-0">
          Add Game
        </Link>
        <Link to={`/wishlist`} className="text-xl font-semibold text-amber-50 p-2 pb-0">
          Wishlist
        </Link>
        <Link to={`/backlog`} className="text-xl font-semibold text-amber-50 p-2 pb-0">
          Backlog
        </Link>
      </div>
      <div className="navbar-end">
        <div className={`flex ${isLoggedIn ? "" : "hidden"}`}>
          <span className="text-xl font-semibold text-amber-50 p-2 pb-0">
            {user?.name}
          </span>
          <div className="dropdown dropdown-end">
            <div
              tabIndex={0}
              role="button"
              className="btn btn-ghost btn-circle avatar"
            >
              <div className="w-10 rounded-full">
                <img
                  alt="Tailwind CSS Navbar component"
                  src={avatarPlaceholder}
                />
              </div>
            </div>
            <ul
              tabIndex={0}
              className="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
            >
              <li>
                <a onClick={() => handleLogoutClick()}>
                  Logout
                </a>
              </li>
            </ul>
          </div>
        </div>
        <div className={isLoggedIn ? "hidden" : ""}>
          <Link to={`/login`} className="text-l font-semibold text-amber-50">
            Login
          </Link>
          <a className="text-white">/</a>
          <Link to={`/registration`} className="text-l font-semibold text-amber-50">
            Sign up
          </Link>
        </div>
      </div>
    </div>
  );
}

export default CustomNavBar;
