import gsap from 'gsap';
import { useGSAP } from '@gsap/react';
import logoImg from '../../assets/images/page_images/main_logo_ivory.png';

const CreateBubbles = () => {
    const bubbles = Array.from({ length: 20 }).map((_, i) => (
        <img 
            src={logoImg}
            key={i}
            id={`bubble_${i}`}
            className='rounded-full h-20 w-20 absolute'
        />
    ));

    useGSAP(() => {
        bubbles.forEach((_, i) => {
            gsap.fromTo(`#bubble_${i}`, {
                x: Math.random() * window.innerWidth,
                y: Math.random() * window.innerHeight,
            }, {
                x: Math.random() * window.innerWidth,
                y: Math.random() * window.innerHeight,
                repeat: -1,
                yoyo: true,
                ease: 'sine.inOut',
                duration: 20, 
            })
        })
    }, [])

    return bubbles;
}

export default CreateBubbles;