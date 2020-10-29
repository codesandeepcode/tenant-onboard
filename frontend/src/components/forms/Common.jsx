import React from 'react';

export const InputTextField = ({ label, required, field, meta, ...props }) => (
    <div className='form-group'>
        <label>
            {label} {required ? <span className='imp'>*</span> : null}
        </label>

        <input
            type='text'
            className={
                'form-control' +
                (meta.error && meta.touched ? ' is-invalid' : '')
            }
            {...field}
            {...props}
        />

        {meta.touched && meta.error ? (
            <div className='invalid-feedback'>{meta.error}</div>
        ) : null}
    </div>
);

export const TextAreaField = ({ label, required, field, meta, ...props }) => (
    <div className='form-group'>
        <label>
            {label} {required ? <span className='imp'>*</span> : null}
        </label>

        <textarea
            rows='2'
            className={
                'form-control' +
                (meta.error && meta.touched ? ' is-invalid' : '')
            }
            {...field}
            {...props}
        />

        {meta.touched && meta.error ? (
            <div className='invalid-feedback'>{meta.error}</div>
        ) : null}
    </div>
);

export const SelectField = ({
    label,
    required,
    field,
    meta,
    data: {
        defaultLabel,
        options: { id, value, list },
    },
    ...props
}) => (
    <div className='form-group'>
        <label>
            {label} {required ? <span className='imp'>*</span> : null}
        </label>

        <select
            className={
                'custom-select' +
                (meta.error && meta.touched ? ' is-invalid' : '')
            }
            {...field}
            {...props}
        >
            <option value=''>{defaultLabel}</option>
            {list.map((option) => (
                <option key={option[id]} value={option[id]}>
                    {option[value]}
                </option>
            ))}
        </select>

        {meta.touched && meta.error ? (
            <div className='invalid-feedback'>{meta.error}</div>
        ) : null}
    </div>
);

export const SpecialCheckboxField = ({
    label,
    required,
    field,
    meta,
    ...props
}) => (
    <div className='form-group'>
        <div className='form-check'>
            <input
                id={field.name}
                type='checkbox'
                checked={field.value}
                className={
                    'form-check-input' +
                    (meta.error && meta.touched ? ' is-invalid' : '')
                }
                {...field}
                {...props}
            />

            <label className='form-check-label' htmlFor={field.name}>
                {label} {required ? <span className='imp'>*</span> : null}
            </label>

            {meta.touched && meta.error ? (
                <div className='invalid-feedback is-invalid'>{meta.error}</div>
            ) : null}
        </div>
    </div>
);
