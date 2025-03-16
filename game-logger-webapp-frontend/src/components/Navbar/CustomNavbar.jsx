import { Link } from "react-router-dom";

function CustomNavbar() {
    return (
        <nav>
            <Link to='/'>IHNtP</Link>
            <Link to='/user/:user_name/wishlist'>Wishlist</Link>
            <Link to='/user/:user_name/played_games'>Played Games</Link>
        </nav>
    )
}

export default CustomNavbar;
