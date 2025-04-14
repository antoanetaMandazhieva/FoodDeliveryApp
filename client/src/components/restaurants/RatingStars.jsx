const FILLED_COLOR = '#ECA447';
const EMPTY_COLOR = '#FFF3E6';

const Star = ({ fillPercentage, id, rating }) => {
    const fillAttr = (fillPercentage === 100)
        ? FILLED_COLOR
        : fillPercentage === 0
        ? EMPTY_COLOR
        : `url(#star-gradient-${rating}-${id})`;

    return (
        <svg
            xmlns='http://www.w3.org/2000/svg'
            viewBox='0 0 24 24'
            strokeWidth='1.5'
            stroke='currentColor'
            className='size-6'
            fill={fillAttr}
            >
            {/* If partial fill, define a gradient */}
            {fillPercentage > 0 && fillPercentage < 100 && (
                <defs>
                <linearGradient id={`star-gradient-${rating}-${id}`} x1='0%' y1='0%' x2='100%' y2='0%'>
                    {/* The first stop uses FILLED_COLOR */}
                    <stop offset={`${fillPercentage}%`} stopColor={FILLED_COLOR} />
                    {/* The rest uses EMPTY_COLOR */}
                    <stop offset={`${fillPercentage}%`} stopColor={EMPTY_COLOR} />
                </linearGradient>
                </defs>
            )}
            <path
                strokeLinecap='round'
                strokeLinejoin='round'
                d='M11.48 3.499a.562.562 0 0 1 1.04 0l2.125 5.111a.563.563 0 0 0 .475.345l5.518.442c.499.04.701.663.321.988l-4.204 3.602a.563.563 0 0 0-.182.557l1.285 5.385a.562.562 0 0 1-.84.61l-4.725-2.885a.562.562 0 0 0-.586 0L6.982 20.54a.562.562 0 0 1-.84-.61l1.285-5.386a.562.562 0 0 0-.182-.557l-4.204-3.602a.562.562 0 0 1 .321-.988l5.518-.442a.563.563 0 0 0 .475-.345L11.48 3.5Z'
            />
        </svg>
    );
};

const RatingStars = ({ rating }) => {
    const fullStars = Math.floor(rating);
    const fraction = rating - fullStars;

    const stars = Array.from({ length: 5 }, (_, i) => {
        let fillValue = 0;
        if (i < fullStars) {
            fillValue = 100;
        } 
        else if (i === fullStars) {
            fillValue = fraction * 100;
        } 
        else {
            fillValue = 0; 
        }
    
        return <Star key={`rating-${rating}-${i}`} 
            id={i}  
            fillPercentage={fillValue} 
            rating={rating}
            />;
    });

    return stars;
}

export default RatingStars;