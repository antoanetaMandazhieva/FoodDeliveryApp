import React from 'react';
import {
    ClockIcon,
    CheckCircleIcon,
    BoxIcon,
    TruckIcon,
    SmileIcon,
    XCircleIcon
} from 'lucide-react'; 

const STEPS = [
    { key: 'PENDING',      label: 'Pending',      icon: ClockIcon },
    { key: 'ACCEPTED',     label: 'Accepted',     icon: CheckCircleIcon },
    { key: 'PREPARING',    label: 'Preparing',    icon: BoxIcon },
    { key: 'IN_DELIVERY',  label: 'In Delivery',  icon: TruckIcon },
    { key: 'DELIVERED',    label: 'Delivered',    icon: SmileIcon },
    { key: 'CANCELLED',    label: 'Cancelled',    icon: XCircleIcon },
];

export default function OrderTrackerVisual({ status }) {
    const isCancelled = status === 'CANCELLED';
    const currentIndex = STEPS.findIndex(s => s.key === status);

    return (
        <div className='max-sm:grid max-sm:grid-rows-3 max-sm:grid-cols-4 flex items-center justify-between w-full max-w-4xl mx-auto px-4 py-8'>
        {STEPS.map((step, idx) => {
            const isDone    = idx < currentIndex && !isCancelled;
            const isActive  = idx === currentIndex && !isCancelled;
            const isFuture  = idx > currentIndex && !isCancelled;
            const isCancelNode = isCancelled && idx === STEPS.length - 1;

            const colorDone   = 'text-peach-300';
            const colorActive = 'animate-pulse';
            const colorFuture = 'text-zinc-600';
            const colorCancel = 'text-red-600';

            let circleColor;
            if (isCancelNode) circleColor = colorCancel;
            else if (isDone) circleColor = colorDone;
            else if (isActive) circleColor = colorActive;
            else circleColor = colorFuture;

            const Line = () => (
            idx < STEPS.length - 1 && (
                <div
                    className={`flex-1 h-1 mx-2 ${
                        isDone ? 'bg-peach-300' : 'bg-zinc-300'
                    }`}
                />
            )
        );

        return (
            <React.Fragment key={step.key}>
                <div className='flex flex-col items-center'>
                    <step.icon className={`size-8 ${circleColor}`} />
                    <span className={`mt-2 text-sm ${circleColor}`}>
                        {step.label}
                    </span>
                </div>
                <Line />
            </React.Fragment>
        );})}
        </div>
    );
}
